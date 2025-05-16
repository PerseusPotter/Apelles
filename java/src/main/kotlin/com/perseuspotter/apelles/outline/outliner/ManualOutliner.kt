package com.perseuspotter.apelles.outline.outliner

import com.perseuspotter.apelles.outline.EntityOutlineRenderer
import com.perseuspotter.apelles.state.Color
import net.minecraft.entity.Entity
import java.util.Collections
import java.util.WeakHashMap

class ManualOutliner(
    color: Color,
    width: Int,
    phase: Boolean,
    chroma: Boolean,
    blackOutline: Boolean,
    absoluteSize: Boolean
) : Outliner(color, width, phase, chroma, blackOutline, absoluteSize) {
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