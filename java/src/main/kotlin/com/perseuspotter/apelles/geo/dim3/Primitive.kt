package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.geo.Geometry

object Primitive : Geometry() {
    override val name = "primitive"
    override fun render(pt: Double, params: List<Double>) {
        begin(params[0].toInt(), false, params[1], params[2], params[3])
        val iter = params.listIterator(1)
        while (iter.hasNext()) {
            val (x, y, z) = rescale(iter.next(), iter.next(), iter.next())
            pos(x, y, z)
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: List<Double>): Int = params.size / 3
    override fun getIndicesCount(params: List<Double>): Int = params.size / 3
    override fun getDrawMode(params: List<Double>): Int = params[0].toInt()
}