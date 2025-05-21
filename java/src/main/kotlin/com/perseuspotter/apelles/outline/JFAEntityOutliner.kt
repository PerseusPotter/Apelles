package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.depression.Framebuffer
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.outline.shader.jfa.JFAInit
import com.perseuspotter.apelles.outline.shader.jfa.JFAPass
import com.perseuspotter.apelles.outline.shader.jfa.JFARender
import com.perseuspotter.apelles.outline.shader.UBOColorShader
import com.perseuspotter.apelles.state.GlState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.culling.Frustum
import org.lwjgl.opengl.ContextCapabilities
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.sqrt

object JFAEntityOutliner : EntityOutliner(1, "JFA") {
    override fun checkCapabilities(cap: ContextCapabilities) = cap.OpenGL31

    var fb1: Framebuffer? = null
    var fb2: Framebuffer? = null

    override fun renderSetup() {
        if (fb1 == null) {
            fb1 = createFB(false)
            fb2 = createFB(false)
        }

        val width = Minecraft.getMinecraft().displayWidth
        val height = Minecraft.getMinecraft().displayHeight
        if (fb1!!.width != width || fb1!!.height != height) {
            fb1!!.createAndCheck(width, height)
            fb2!!.createAndCheck(width, height)
        }

        fb1!!.clear(GL11.GL_COLOR_BUFFER_BIT)
        copyDepth(fb1!!)
    }

    private val jfaTransformer = object : Framebuffer.Companion.ColorTransformer() {
        override fun r(v: Float): Float = sqrt(v) / 255f
        override fun g(v: Float): Float = v / 1000f
        override fun b(v: Float): Float = v / 1000f
    }

    private fun ceilLog2(x: Int): Int = 32 - (x - 1).coerceAtLeast(0).countLeadingZeroBits()
    override fun renderPass(pt: Double, t: Int, ents: List<OutlineState>, pass: Int) {
        val prof = Minecraft.getMinecraft().mcProfiler
        val rm = Minecraft.getMinecraft().renderManager
        val mainFb = Minecraft.getMinecraft().framebuffer

        OpenGlHelper.glBlendFunc(1, 0, 1, 0)
        GL11.glDisable(GL11.GL_BLEND)

        fb1!!.bindFramebuffer()
        JFAInit.bind()
        JFAInit.setSize(fb1!!.textureWidth, fb1!!.textureHeight)
        var maxW = 0
        val colors = UBOColorShader.ColorBuilder()
        var prevCol = -1
        if (dump) {
            val depthImage = fb1!!.dumpDepth()
            ImageIO.write(depthImage, "png", File("./$name-depthBufferPre$pass.png"))
            println("outlining entities pass $pass: ${ents.joinToString(" ") { it.entity.get()!!.entityId.toString() }}")
        }

        prof.startSection("seeding")
        val frust = Frustum()
        frust.setPosition(Geometry.getRenderX(), Geometry.getRenderY(), Geometry.getRenderZ())
        ents.groupBy { it.getWidth() }.forEach { (w, e) ->
            JFAInit.setWidth(w)
            // "fixed"
            val w2 = if (w < 0) -32 * w else w
            if (w2 > maxW) maxW = w2
            e.forEach Inner@ {
                val ent = it.entity.get()!!
                if (!frust.isBoundingBoxInFrustum(ent.entityBoundingBox)) return@Inner
                val id = colors.getId(it.getColor())
                if (id != prevCol) {
                    JFAInit.setColorId(id)
                    prevCol = id
                }
                val invis = it.renderInvis() && ent.isInvisible
                if (invis) ent.isInvisible = false
                rm.renderEntityStatic(ent, pt.toFloat(), false)
                if (invis) ent.isInvisible = true
            }
        }
        GlState.reset()
        if (dump) {
            fb1!!.bindFramebuffer()
            val colorImage = fb1!!.dumpColor(jfaTransformer)
            val depthImage = fb1!!.dumpDepth()
            ImageIO.write(colorImage, "png", File("./$name-colorBufferInit$pass.png"))
            ImageIO.write(depthImage, "png", File("./$name-depthBufferInit$pass.png"))
        }
        GlState.setDepthTest(false)

        prof.endStartSection("iters")
        JFAPass.bind()
        JFAPass.setSize(fb1!!.textureWidth, fb1!!.textureHeight)
        var f = true
        var iter = 0
        fun doIter(gap: Int) {
            JFAPass.setGap(gap)
            val fb = if (f) fb1 else fb2
            val pp = if (f) fb2 else fb1
            f = !f
            fb!!.bindTexture()
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb.framebufferObject)
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, pp!!.framebufferObject)
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
            if (dump) {
                pp.bindFramebuffer()
                val colorImage = pp.dumpColor(jfaTransformer)
                ImageIO.write(colorImage, "png", File("./$name-colorBufferPass$pass$iter.png"))
            }
            iter++
        }
        for (i in ceilLog2(maxW) - 1 downTo 0) {
            doIter(1 shl i)
        }
        doIter(2)
        doIter(1)

        prof.endStartSection("render")
        GL11.glEnable(GL11.GL_BLEND)
        OpenGlHelper.glBlendFunc(770, 771, 1, 771)
        JFARender.bind()
        JFARender.bindUbo()
        JFARender.setColors(colors.toList())
        (if (f) fb1 else fb2)!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, (if (f) fb1 else fb2)!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, mainFb.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        JFARender.unbindUbo()
        prof.endSection()
        fb1!!.unbindTexture()
    }

    override fun renderCleanup1() {
        fb1!!.clear(GL11.GL_COLOR_BUFFER_BIT)
    }

    override fun renderCleanup2() {
        GlState.bindShader(0)
        Minecraft.getMinecraft().framebuffer.bindFramebuffer(false)
    }
}