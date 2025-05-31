package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.outline.outliner.RenderOutliner
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GLContext
import java.util.*

object EntityOutlineRenderer {
    val outlined = WeakHashMap<Entity, OutlineState>()
    fun getOutlineState(e: Entity): OutlineState = outlined.getOrPut(e) { OutlineState(e) }
    val outliners = linkedSetOf<RenderOutliner>()
    val outlineRenderers = arrayOf(JFAEntityOutliner, RobertsCrossEntityOutliner, SobelEntityOutliner, BlurEntityOutliner)

    private var checked = false
    fun checkEntities(pt: Double) {
        if (!checked) {
            val cap = GLContext.getCapabilities()
            outlineRenderers.forEach { it.CAN_OUTLINE = it.checkCapabilities(cap) }
            checked = true
        }
        val prof = Minecraft.getMinecraft().mcProfiler
        prof.startSection("testEntities")
        if (outliners.isNotEmpty()) {
            Minecraft.getMinecraft().theWorld.loadedEntityList.forEach { e ->
                // good enough
                val x = e.lastTickPosX + (e.posX - e.lastTickPosX) * pt
                val y = e.lastTickPosY + (e.posY - e.lastTickPosY) * pt
                val z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * pt
                if (!Frustum.test(x, y, z)) return@forEach
                outliners.forEach { if (it.registered) it.test(e) }
            }
        }
        outlineRenderers.forEach { it.clear() }
        if (outlined.isNotEmpty()) {
            val loadedEntities = Minecraft.getMinecraft().theWorld.loadedEntityList.toSet()
            outlined.entries.removeIf { (e, s) ->
                if (!loadedEntities.contains(e)) return@removeIf true
                if (e.isDead) return@removeIf true
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && e is EntityPlayerSP) return@removeIf false
                if (s.getOutlineType() == 0) return@removeIf false
                if (s.getColor().a == 0f) return@removeIf false
                if (!Frustum.checkAABB(e.entityBoundingBox)) return@removeIf false
                outlineRenderers[s.getOutlineType() - 1].add(s)
                return@removeIf false
            }
        }
        prof.endSection()
        outlineRenderers.forEach { it.endPrepare(pt) }
    }

    fun renderOutlines(pt: Double) {
        Minecraft.getMinecraft().renderManager.setRenderOutlines(true)
        outlineRenderers.forEach { it.render(pt) }
        Minecraft.getMinecraft().renderManager.setRenderOutlines(false)

        outliners.forEach { it._internalClear() }
        EntityOutliner.dump = false
    }
}