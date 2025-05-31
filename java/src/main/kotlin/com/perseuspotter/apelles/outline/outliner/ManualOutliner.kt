package com.perseuspotter.apelles.outline.outliner

import com.perseuspotter.apelles.outline.EntityOutlineRenderer
import com.perseuspotter.apelles.state.Color
import net.minecraft.entity.Entity
import java.util.*

class ManualOutliner(
    color: Color,
    type: Int,
    width: Int,
    phase: Boolean,
    chroma: Boolean,
    blackOutline: Boolean,
    absoluteSize: Boolean,
    renderInvis: Boolean
) : Outliner(color, type, width, phase, chroma, blackOutline, absoluteSize, renderInvis) {
    val hits = Collections.newSetFromMap(WeakHashMap<Entity, Boolean>())

    fun add(e: Entity) {
        if (hits.add(e) && registered) EntityOutlineRenderer.getOutlineState(e).add(this)
    }
    fun remove(e: Entity) {
        if (hits.remove(e) && registered) EntityOutlineRenderer.getOutlineState(e).remove(this)
    }
    fun clear() {
        hits.forEach { EntityOutlineRenderer.getOutlineState(it).remove(this) }
        hits.clear()
    }

    override fun register() {
        hits.forEach { EntityOutlineRenderer.getOutlineState(it).add(this) }
        super.register()
    }
    override fun unregister() {
        hits.forEach { EntityOutlineRenderer.getOutlineState(it).remove(this) }
        super.unregister()
    }
}