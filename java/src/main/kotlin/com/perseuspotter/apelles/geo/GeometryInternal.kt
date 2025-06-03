package com.perseuspotter.apelles.geo

abstract class GeometryInternal : Geometry() {
    override fun render(pt: Double, params: List<Double>) = throw UnsupportedOperationException()
    abstract fun render(pt: Double, params: DoubleArray, N: Int)
    override fun getVertexCount(params: List<Double>): Int = throw UnsupportedOperationException()
    abstract fun getVertexCount(params: DoubleArray, N: Int): Int
    override fun getIndicesCount(params: List<Double>): Int = throw UnsupportedOperationException()
    abstract fun getIndicesCount(params: DoubleArray, N: Int): Int
    override fun getDrawMode(params: List<Double>): Int = throw UnsupportedOperationException()
    abstract fun getDrawMode(params: DoubleArray, N: Int): Int
}