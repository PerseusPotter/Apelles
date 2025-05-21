package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.depression.Framebuffer
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.outline.shader.sobel.SobelInit
import com.perseuspotter.apelles.outline.shader.sobel.SobelRender
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
import kotlin.math.abs

object SobelEntityOutliner : EntityOutliner(3, "Sobel") {
    override fun checkCapabilities(cap: ContextCapabilities): Boolean = cap.OpenGL30

    var fb: Framebuffer? = null

    override fun renderSetup() {
        if (fb == null) fb = createFB(false)

        val width = Minecraft.getMinecraft().displayWidth
        val height = Minecraft.getMinecraft().displayHeight
        if (fb!!.width != width || fb!!.height != height) fb!!.createAndCheck(width, height)

        fb!!.clear(GL11.GL_COLOR_BUFFER_BIT)
        copyDepth(fb!!)
    }

    private val sobelTransformer = object : Framebuffer.Companion.ColorTransformer() {
        override fun a(v: Float) = abs(v)
    }

    override fun renderPass(pt: Double, t: Int, ents: List<OutlineState>, pass: Int) {
        val prof = Minecraft.getMinecraft().mcProfiler
        val rm = Minecraft.getMinecraft().renderManager
        val mainFb = Minecraft.getMinecraft().framebuffer

        OpenGlHelper.glBlendFunc(1, 0, 1, 0)
        GL11.glDisable(GL11.GL_BLEND)

        fb!!.bindFramebuffer()
        SobelInit.bind()
        prof.startSection("render")
        val frust = Frustum()
        frust.setPosition(Geometry.getRenderX(), Geometry.getRenderY(), Geometry.getRenderZ())
        ents.forEach {
            val ent = it.entity.get()!!
            if (!frust.isBoundingBoxInFrustum(ent.entityBoundingBox)) return@forEach
            SobelInit.setColor(it.getColor())
            val invis = it.renderInvis() && ent.isInvisible
            if (invis) ent.isInvisible = false
            rm.renderEntityStatic(ent, pt.toFloat(), false)
            if (invis) ent.isInvisible = true
        }
        GlState.reset()
        GlState.setDepthTest(false)

        if (dump) {
            val colorImage = fb!!.dumpColor(sobelTransformer)
            val depthImage = fb!!.dumpDepth()
            ImageIO.write(colorImage, "png", File("./$name-colorBuffer$pass.png"))
            ImageIO.write(depthImage, "png", File("./$name-depthBuffer$pass.png"))
        }

        prof.endStartSection("blit")
        SobelRender.bind()
        GL11.glEnable(GL11.GL_BLEND)
        OpenGlHelper.glBlendFunc(770, 771, 1, 771)
        fb!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, mainFb.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        prof.endSection()
        fb!!.unbindTexture()
    }

    override fun renderCleanup1() {
        fb!!.clear(GL11.GL_COLOR_BUFFER_BIT)
    }

    override fun renderCleanup2() {
        GlState.bindShader(0)
        Minecraft.getMinecraft().framebuffer.bindFramebuffer(false)
    }
}