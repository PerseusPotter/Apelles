package com.perseuspotter.apelles.depression

class DoubleArrayList(var capacity: Int = 10) {
    var elems = DoubleArray(capacity)
    var length = 0
    fun add(v: Double) {
        if (length == capacity) resize()
        elems[length++] = v
    }
    fun resize() {
        capacity = capacity + capacity shr 1
        val newElems = DoubleArray(capacity)
        elems.copyInto(newElems)
        elems = newElems
    }
}