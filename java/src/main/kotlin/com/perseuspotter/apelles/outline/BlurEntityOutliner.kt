package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.depression.Framebuffer
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.outline.shader.blur.BlurHorzPass
import com.perseuspotter.apelles.outline.shader.blur.BlurInit
import com.perseuspotter.apelles.outline.shader.blur.BlurRender
import com.perseuspotter.apelles.outline.shader.blur.BlurVertPass
import com.perseuspotter.apelles.state.GlState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.culling.Frustum
import org.lwjgl.opengl.ContextCapabilities
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.pow

object BlurEntityOutliner : EntityOutliner(4, "Blur") {
    override fun checkCapabilities(cap: ContextCapabilities): Boolean = cap.OpenGL30

    var fb1: Framebuffer? = null
    var fb2: Framebuffer? = null

    override fun renderSetup() {
        if (fb1 == null) {
            fb1 = createFB(true)
            fb2 = createFB(true)
        }

        val width = Minecraft.getMinecraft().displayWidth
        val height = Minecraft.getMinecraft().displayHeight
        if (fb1!!.width != width || fb1!!.height != height) {
            fb1!!.createAndCheck(width, height)
            fb2!!.createAndCheck(width, height)
        }

        fb1!!.clear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)
        copyDepth(fb1!!)
    }

    private val blurTransformer = object : Framebuffer.Companion.ColorTransformer() {
        override fun a(v: Float) = abs(v)
    }

    override fun renderPass(pt: Double, t: Int, ents: List<OutlineState>, pass: Int) {
        val prof = Minecraft.getMinecraft().mcProfiler
        val rm = Minecraft.getMinecraft().renderManager
        val mainFb = Minecraft.getMinecraft().framebuffer

        OpenGlHelper.glBlendFunc(1, 0, 1, 0)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_STENCIL_TEST)
        GL11.glStencilMask(0xFF)
        GL11.glStencilFunc(GL11.GL_ALWAYS, 255, 0xFF)
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE)

        fb1!!.bindFramebuffer()
        BlurInit.bind()
        prof.startSection("render")
        val frust = Frustum()
        frust.setPosition(Geometry.getRenderX(), Geometry.getRenderY(), Geometry.getRenderZ())
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
            if (!frust.isBoundingBoxInFrustum(ent.entityBoundingBox)) return@forEach
            BlurInit.setColor(it.getColor())
            val invis = it.renderInvis() && ent.isInvisible
            if (invis) ent.isInvisible = false
            rm.renderEntityStatic(ent, pt.toFloat(), false)
            if (invis) ent.isInvisible = true
        }
        GlState.reset()
        GlState.setDepthTest(false)
        GL11.glDisable(GL11.GL_STENCIL_TEST)

        if (dump) {
            fb1!!.bindFramebuffer()
            val colorImage = fb1!!.dumpColor(blurTransformer)
            val depthImage = fb1!!.dumpDepth()
            val stencilImage = fb1!!.dumpStencil()
            ImageIO.write(colorImage, "png", File("./$name-colorBufferInit$pass.png"))
            ImageIO.write(depthImage, "png", File("./$name-depthBufferInit$pass.png"))
            ImageIO.write(stencilImage, "png", File("./$name-stencilBufferInit$pass.png"))
        }

        prof.endStartSection("blur")
        BlurVertPass.bind()
        fb1!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb1!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb2!!.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        if (dump) {
            fb2!!.bindFramebuffer()
            val colorImage = fb2!!.dumpColor(blurTransformer)
            ImageIO.write(colorImage, "png", File("./$name-colorBufferVert1$pass.png"))
        }
        BlurHorzPass.bind()
        fb2!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb2!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb1!!.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        if (dump) {
            fb1!!.bindFramebuffer()
            val colorImage = fb1!!.dumpColor(blurTransformer)
            ImageIO.write(colorImage, "png", File("./$name-colorBufferHorz2$pass.png"))
        }
        BlurVertPass.bind()
        fb1!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb1!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb2!!.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        if (dump) {
            fb2!!.bindFramebuffer()
            val colorImage = fb2!!.dumpColor(blurTransformer)
            ImageIO.write(colorImage, "png", File("./$name-colorBufferVert3$pass.png"))
        }
        BlurHorzPass.bind()
        fb2!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb2!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb1!!.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        if (dump) {
            fb1!!.bindFramebuffer()
            val colorImage = fb1!!.dumpColor(blurTransformer)
            ImageIO.write(colorImage, "png", File("./$name-colorBufferHorz4$pass.png"))
        }

        prof.endStartSection("blit")
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb1!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb2!!.framebufferObject)
        GL30.glBlitFramebuffer(0, 0, fb1!!.width, fb1!!.height, 0, 0, fb2!!.width, fb2!!.height, GL11.GL_STENCIL_BUFFER_BIT, GL11.GL_NEAREST)
        fb2!!.clear(GL11.GL_COLOR_BUFFER_BIT)
        BlurRender.bind()
        GL11.glEnable(GL11.GL_STENCIL_TEST)
        GL11.glStencilFunc(GL11.GL_EQUAL, 0, 0xFF)
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP)
        fb1!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb1!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb2!!.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        GL11.glDisable(GL11.GL_STENCIL_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        OpenGlHelper.glBlendFunc(770, 771, 1, 771)
        fb2!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb2!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, mainFb.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        prof.endSection()
        if (dump) {
            fb2!!.bindFramebuffer()
            val colorImage = fb2!!.dumpColor(blurTransformer)
            val depthImage = fb2!!.dumpDepth()
            val stencilImage = fb2!!.dumpStencil()
            ImageIO.write(colorImage, "png", File("./$name-colorBufferPost$pass.png"))
            ImageIO.write(depthImage, "png", File("./$name-depthBufferPost$pass.png"))
            ImageIO.write(stencilImage, "png", File("./$name-stencilBufferPost$pass.png"))
        }
    }

    override fun renderCleanup1() {
        fb1!!.clear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)
    }

    override fun renderCleanup2() {
        GlState.bindShader(0)
        GlState.bindTexture(0)
        Minecraft.getMinecraft().framebuffer.bindFramebuffer(false)
    }
}