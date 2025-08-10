package com.perseuspotter.apelles.geo

abstract class GeometryInternal : Geometry() {
    var currentParamsArr = doubleArrayOf()
    var currentParamsLen = 0
    override fun render(pt: Double) = render(pt, currentParamsArr, currentParamsLen)
    abstract fun render(pt: Double, params: DoubleArray, N: Int)
    override fun getVertexCount(): Int = getVertexCount(currentParamsArr, currentParamsLen)
    abstract fun getVertexCount(params: DoubleArray, N: Int): Int
    override fun getIndexCount(): Int = getIndexCount(currentParamsArr, currentParamsLen)
    abstract fun getIndexCount(params: DoubleArray, N: Int): Int
    override fun inView(): Boolean = inView(currentParamsArr, currentParamsLen)
    abstract fun inView(params: DoubleArray, N: Int): Boolean
    override fun getDrawMode(): Int = getDrawMode(currentParamsArr, currentParamsLen)
    abstract fun getDrawMode(params: DoubleArray, N: Int): Int
}