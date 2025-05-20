package com.perseuspotter.apelles.outline.outliner

import com.perseuspotter.apelles.outline.EntityOutlineRenderer
import com.perseuspotter.apelles.outline.OutlineTester
import com.perseuspotter.apelles.state.Color
import net.minecraft.entity.Entity

abstract class RenderOutliner(
    val tester: OutlineTester,
    color: Color,
    type: Int,
    width: Int,
    phase: Boolean,
    chroma: Boolean,
    blackOutline: Boolean,
    absoluteSize: Boolean,
    renderInvis: Boolean
) : Outliner(color, type, width, phase, chroma, blackOutline, absoluteSize, renderInvis) {
    abstract fun test(e: Entity)
    abstract fun _internalClear()

    override fun register() {
        EntityOutlineRenderer.outliners.add(this)
        super.register()
    }
    override fun unregister() {
        EntityOutlineRenderer.outliners.remove(this)
        super.unregister()
    }
}