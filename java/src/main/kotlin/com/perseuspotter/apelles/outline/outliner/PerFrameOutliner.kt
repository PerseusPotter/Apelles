package com.perseuspotter.apelles.outline.outliner

import com.perseuspotter.apelles.outline.EntityOutlineRenderer
import com.perseuspotter.apelles.outline.OutlineTester
import com.perseuspotter.apelles.state.Color
import net.minecraft.entity.Entity

class PerFrameOutliner(
    tester: OutlineTester,
    color: Color,
    type: Int,
    width: Int,
    phase: Boolean,
    chroma: Boolean,
    blackOutline: Boolean,
    absoluteSize: Boolean,
    renderInvis: Boolean
) : RenderOutliner(tester, color, type, width, phase, chroma, blackOutline, absoluteSize, renderInvis)  {
    val hits = linkedSetOf<Entity>()
    override fun test(e: Entity) {
        if (tester.test(e)) {
            hits.add(e)
            EntityOutlineRenderer.getOutlineState(e).add(this)
        }
    }
    override fun _internalClear() {
        hits.forEach { EntityOutlineRenderer.getOutlineState(it).remove(this) }
        hits.clear()
    }
}