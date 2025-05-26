package com.perseuspotter.apelles.outline.outliner

import com.perseuspotter.apelles.outline.EntityOutlineRenderer
import com.perseuspotter.apelles.outline.OutlineTester
import com.perseuspotter.apelles.state.Color
import net.minecraft.entity.Entity
import java.util.*

class SemiAutomaticOutliner(
    tester: OutlineTester,
    color: Color,
    type: Int,
    width: Int,
    phase: Boolean,
    chroma: Boolean,
    blackOutline: Boolean,
    absoluteSize: Boolean,
    renderInvis: Boolean
) : RenderOutliner(tester, color, type, width, phase, chroma, blackOutline, absoluteSize, renderInvis) {
    val hits = Collections.newSetFromMap(WeakHashMap<Entity, Boolean>())
    val seen = Collections.newSetFromMap(WeakHashMap<Entity, Boolean>())

    override fun test(e: Entity) {
        if (seen.add(e) && tester.test(e)) {
            hits.add(e)
            EntityOutlineRenderer.getOutlineState(e).add(this)
        }
    }
    override fun _internalClear() {}

    override fun register() {
        hits.forEach { EntityOutlineRenderer.getOutlineState(it).add(this) }
        super.register()
    }
    override fun unregister() {
        hits.forEach { EntityOutlineRenderer.getOutlineState(it).remove(this) }
        super.unregister()
    }

    fun clear() {
        hits.forEach { EntityOutlineRenderer.getOutlineState(it).remove(this) }
        hits.clear()
        seen.clear()
    }

    fun add(e: Entity) {
        seen.add(e)
        if (hits.add(e) && registered) EntityOutlineRenderer.getOutlineState(e).add(this)
    }

    fun remove(e: Entity) {
        seen.add(e)
        if (hits.remove(e)) EntityOutlineRenderer.getOutlineState(e).remove(this)
    }

    fun retest(e: Entity) {
        seen.remove(e)
        if (hits.remove(e)) EntityOutlineRenderer.getOutlineState(e).remove(this)
    }
}