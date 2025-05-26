package com.perseuspotter.apelles.outline.outliner

import com.perseuspotter.apelles.state.Color

abstract class Outliner(
    color: Color,
    val type: Int,
    val width: Int,
    val phase: Boolean,
    val chroma: Boolean,
    val blackOutline: Boolean,
    val absoluteSize: Boolean,
    val renderInvis: Boolean
) : Comparable<Outliner> {
    lateinit var col: Color
    init {
        setColor(color)
        when (type) {
            1 -> {}
            2, 3, 4 -> {
                if (width != 1) throw IllegalArgumentException("changing width is only available with JFA")
                if (blackOutline) throw IllegalArgumentException("blackOutline is only available with JFA")
                if (absoluteSize) throw IllegalArgumentException("absoluteSize is only available with JFA")
            }
            else -> throw IllegalArgumentException("not a valid outliner type, found $type")
        }
    }
    fun setColor(col: Long) = setColor(Color(col))
    fun setColor(col: List<Double>) = setColor(Color(col))
    fun setColor(col: Color) {
        this.col = Color(col.r, col.g, negateIf(col.b, blackOutline), negateIf(col.a, chroma))
    }

    private fun negateIf(f: Float, b: Boolean) = if (b) -f - 0.001f else f
    override fun compareTo(other: Outliner): Int {
        if (phase != other.phase) return if (phase) -1 else 1
        if (absoluteSize != other.absoluteSize) return if (absoluteSize) -1 else 1
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