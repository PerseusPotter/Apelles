package com.perseuspotter.apelles.outline.outliner

import com.perseuspotter.apelles.outline.EntityOutlineRenderer
import com.perseuspotter.apelles.outline.OutlineTester
import com.perseuspotter.apelles.state.Color
import net.minecraft.entity.Entity

abstract class RenderOutliner(
    val tester: OutlineTester,
    color: Color,
    width: Int,
    phase: Boolean,
    chroma: Boolean,
    blackOutline: Boolean,
    absoluteSize: Boolean
) : Outliner(color, width, phase, chroma, blackOutline, absoluteSize) {
    abstract fun test(e: Entity)
    abstract fun clear()

    override fun register() {
        EntityOutlineRenderer.outliners.add(this)
        super.register()
    }
    override fun unregister() {
        EntityOutlineRenderer.outliners.remove(this)
        super.unregister()
    }
}