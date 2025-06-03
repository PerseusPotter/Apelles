package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.geo.GeometryInternal

object PrimitiveInternal : GeometryInternal() {
    override val name = "primitiveinternal"
    override fun render(pt: Double, params: DoubleArray, N: Int) {
        begin(params[0].toInt(), false, params[1], params[2], params[3])
        var i = 1
        while (i < N) {
            pos(params[i++], params[i++], params[i++])
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: DoubleArray, N: Int): Int = (N - 1) / 3
    override fun getIndicesCount(params: DoubleArray, N: Int): Int = (N - 1) / 3
    override fun getDrawMode(params: DoubleArray, N: Int): Int = params[0].toInt()
}