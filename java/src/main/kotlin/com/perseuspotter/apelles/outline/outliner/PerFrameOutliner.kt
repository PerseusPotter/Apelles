package com.perseuspotter.apelles.outline.outliner

import com.perseuspotter.apelles.outline.EntityOutlineRenderer
import com.perseuspotter.apelles.outline.OutlineTester
import com.perseuspotter.apelles.state.Color
import net.minecraft.entity.Entity

class PerFrameOutliner(
    tester: OutlineTester,
    color: Color,
    width: Int,
    phase: Boolean,
    chroma: Boolean,
    blackOutline: Boolean,
    absoluteSize: Boolean
) : RenderOutliner(tester, color, width, phase, chroma, blackOutline, absoluteSize)  {
    val hits = linkedSetOf<Entity>()
    override fun test(e: Entity) {
        if (tester.test(e)) {
            hits.add(e)
            EntityOutlineRenderer.getOutlineState(e).add(this)
        }
    }
    override fun clear() {
        hits.forEach { EntityOutlineRenderer.getOutlineState(it).remove(this) }
        hits.clear()
    }
}