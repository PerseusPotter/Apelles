package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.depression.Framebuffer
import com.perseuspotter.apelles.state.GlState
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.ContextCapabilities
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30

abstract class EntityOutliner(val type: Int, val name: String) {
    val phase = mutableListOf<OutlineState>()
    val occluded = mutableListOf<OutlineState>()

    var CAN_OUTLINE = false
    abstract fun checkCapabilities(cap: ContextCapabilities): Boolean

    fun clear() {
        phase.clear()
        occluded.clear()
    }
    fun add(s: OutlineState) {
        if (s.getPhase()) phase.add(s)
        else occluded.add(s)
    }
    fun endPrepare(pt: Double) {
        if (CAN_OUTLINE) return
        phase.forEach {
            val ent = it.entity.get()!!
            val x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pt
            val y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pt
            val z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pt
            val w = ent.width
            val h = ent.height
            Renderer.addAABBO(it.getColor(), x - w / 2.0, y, z - w / 2.0, x + w / 2.0, y + h, z + w / 2.0, it.getWidth().toDouble(), 0, true, false, true, if (it.isChroma()) 1 else 0)
        }
        occluded.forEach {
            val ent = it.entity.get()!!
            val x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pt
            val y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pt
            val z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pt
            val w = ent.width
            val h = ent.height
            Renderer.addAABBO(it.getColor(), x - w / 2.0, y, z - w / 2.0, x + w / 2.0, y + h, z + w / 2.0, it.getWidth().toDouble(), 0, false, false, true, if (it.isChroma()) 1 else 0)
        }
    }
    fun render(pt: Double, t: Int) {
        if (!CAN_OUTLINE || (phase.size == 0 && occluded.size == 0)) return

        val prof = Minecraft.getMinecraft().mcProfiler
        prof.startSection(name)

        prof.startSection("setup")
        GlState.setLighting(0)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glDepthMask(false)
        renderSetup()

        prof.endStartSection("phase")
        if (phase.size > 0) {
            GlState.setDepthTest(false)
            renderPass(pt, t, phase, 0)
            if (occluded.size > 0) renderCleanup1()
        }

        prof.endStartSection("occluded")
        if (occluded.size > 0) {
            GlState.setDepthTest(true)
            renderPass(pt, t, occluded, 1)
        }
        GL11.glDepthMask(true)
        renderCleanup2()
        prof.endSection()

        prof.endSection()
    }

    protected fun createFB(stencil: Boolean): Framebuffer {
        val main = Minecraft.getMinecraft().framebuffer
        val fb = Framebuffer(main.framebufferTextureWidth, main.framebufferTextureHeight, true, stencil)
        fb.setColor(0.0f, 0.0f, 0.0f, 0.0f)
        fb.bindFramebuffer()
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        fb.unbindFramebuffer()
        return fb
    }

    protected fun copyDepth(fb: Framebuffer) {
        val mainFb = Minecraft.getMinecraft().framebuffer
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainFb.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb.framebufferObject)
        GL30.glBlitFramebuffer(0, 0, mainFb.framebufferWidth, mainFb.framebufferHeight, 0, 0, fb.width, fb.height, GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST)
    }

    protected abstract fun renderSetup()
    protected abstract fun renderPass(pt: Double, t: Int, ents: List<OutlineState>, pass: Int)
    protected open fun renderCleanup1() {}
    protected open fun renderCleanup2() {}

    companion object {
        @JvmField
        var dump = false
    }
}