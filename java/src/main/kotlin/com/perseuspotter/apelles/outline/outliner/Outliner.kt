package com.perseuspotter.apelles.outline.outliner

import com.perseuspotter.apelles.state.Color

abstract class Outliner(
    color: Color,
    val width: Int,
    val phase: Boolean,
    val chroma: Boolean,
    val blackOutline: Boolean,
    val absoluteSize: Boolean
) : Comparable<Outliner> {
    private fun negateIf(f: Float, b: Boolean) = if (b) -f - 0.001f else f
    val color = Color(color.r, color.g, negateIf(color.b, blackOutline), negateIf(color.a, chroma))
    override fun compareTo(other: Outliner): Int {
        if (phase != other.phase) return if (phase) -1 else 1
        return -(width.compareTo(other.width))
    }

    var registered = false
    open fun register() {
        if (registered) return
        registered = true
    }
    open fun unregister() {
        if (!registered) return
        registered = false
    }
}