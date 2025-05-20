package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.state.GlState
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.ContextCapabilities
import org.lwjgl.opengl.GL11

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
        renderSetup()

        prof.endStartSection("phase")
        if (phase.size > 0) {
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            renderPass(pt, t, phase, 0)
            renderCleanup1()
        }

        prof.endStartSection("occluded")
        if (occluded.size > 0) {
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GlState.setDepthTest(true)
            renderPass(pt, t, occluded, 1)
            renderCleanup2()
        }
        renderCleanup3()
        prof.endSection()

        prof.endSection()
    }

    abstract fun renderSetup()
    abstract fun renderPass(pt: Double, t: Int, ents: List<OutlineState>, pass: Int)
    open fun renderCleanup1() {}
    open fun renderCleanup2() {}
    open fun renderCleanup3() {}
}