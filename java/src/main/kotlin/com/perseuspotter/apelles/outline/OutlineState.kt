package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.outline.outliner.Outliner
import com.perseuspotter.apelles.state.Color
import net.minecraft.entity.Entity
import java.lang.ref.WeakReference
import java.util.PriorityQueue

class OutlineState(entity: Entity) {
    val entity = WeakReference(entity)
    private val outliners = PriorityQueue<Outliner>()
    private var active: Outliner? = null

    private fun update() {
        active = outliners.peek()
    }
    fun add(o: Outliner) {
        outliners.add(o)
        update()
    }
    fun remove(o: Outliner) {
        outliners.remove(o)
        update()
    }
    fun getOutlineType(): Int = active?.type ?: 0
    fun getColor(): Color = active!!.col
    // shhh dont tell anyone
    fun getWidth(): Int = (active!!.width + 1) * (if (isAbsoluteSize()) -1 else 1)
    fun getPhase(): Boolean = active!!.phase
    fun isChroma(): Boolean = active!!.chroma
    fun isAbsoluteSize(): Boolean = active!!.absoluteSize
    fun renderInvis(): Boolean = active!!.renderInvis
}