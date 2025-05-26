package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.depression.ChromaShader
import com.perseuspotter.apelles.depression.Framebuffer
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.outline.shader.OutlineInit
import com.perseuspotter.apelles.state.GlState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.ContextCapabilities
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.pow

abstract class KernelEntityOutliner(type: Int, name: String, val renderShader: ChromaShader) : EntityOutliner(type, name) {
    override fun checkCapabilities(cap: ContextCapabilities): Boolean = cap.OpenGL30

    var fb: Framebuffer? = null

    override fun renderSetup() {
        if (fb == null) fb = createFB(false, false)

        val width = Minecraft.getMinecraft().displayWidth
        val height = Minecraft.getMinecraft().displayHeight
        if (fb!!.width != width || fb!!.height != height) fb!!.createAndCheck(width, height)

        fb!!.clear(GL11.GL_COLOR_BUFFER_BIT)
        copyDepth(fb!!)

        GL11.glDepthMask(true)
    }

    private val transformer = object : Framebuffer.Companion.ColorTransformer() {
        override fun a(v: Float) = abs(v)
    }

    override fun renderPass(pt: Double, t: Int, ents: List<OutlineState>, pass: Int) {
        val prof = Minecraft.getMinecraft().mcProfiler
        val rm = Minecraft.getMinecraft().renderManager
        val mainFb = Minecraft.getMinecraft().framebuffer

        OpenGlHelper.glBlendFunc(1, 0, 1, 0)
        GL11.glDisable(GL11.GL_BLEND)

        fb!!.bindFramebuffer()
        OutlineInit.bind()
        prof.startSection("render")
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0)
        GlStateManager.bindTexture(0)
        ents.sortedByDescending {
            val ent = it.entity.get()!!
            val x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pt
            val y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pt
            val z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pt
            (Geometry.getRenderX() - x).pow(2) + (Geometry.getRenderY() - y).pow(2) + (Geometry.getRenderZ() - z).pow(2)
        }.forEach {
            val ent = it.entity.get()!!
            OutlineInit.setColor(it.getColor())
            val invis = it.renderInvis() && ent.isInvisible
            if (invis) ent.isInvisible = false
            rm.renderEntityStatic(ent, pt.toFloat(), false)
            if (invis) ent.isInvisible = true
        }
        GlState.reset()
        GlState.setDepthTest(false)

        if (dump) {
            val colorImage = fb!!.dumpColor(transformer)
            val depthImage = fb!!.dumpDepth()
            ImageIO.write(colorImage, "png", File("./$name-colorBuffer$pass.png"))
            ImageIO.write(depthImage, "png", File("./$name-depthBuffer$pass.png"))
        }

        prof.endStartSection("blit")
        renderShader.bind()
        GL11.glEnable(GL11.GL_BLEND)
        OpenGlHelper.glBlendFunc(770, 771, 1, 771)
        fb!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, mainFb.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        prof.endSection()
    }

    override fun renderCleanup1() {
        fb!!.clear(GL11.GL_COLOR_BUFFER_BIT)
    }

    override fun renderCleanup2() {
        GlState.bindShader(0)
        GlState.bindTexture(0)
        Minecraft.getMinecraft().framebuffer.bindFramebuffer(false)
    }
}