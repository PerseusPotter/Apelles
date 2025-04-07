package com.perseuspotter.apelles.state

data class Color @JvmOverloads constructor(
    @JvmField
    val r: Float = 0f,
    @JvmField
    val g: Float = 0f,
    @JvmField
    val b: Float = 0f,
    @JvmField
    val a: Float = 0f
) {
    constructor(packed: Long) : this(
        ((packed ushr 24) and 0xFF) / 255f,
        ((packed ushr 16) and 0xFF) / 255f,
        ((packed ushr 8) and 0xFF) / 255f,
        (packed and 0xFF) / 255f
    )
    constructor(arr: FloatArray) : this(arr[0], arr[1], arr[2], arr.getOrElse(3) { 1f })
}