package com.perseuspotter.apelles.geo.dim3.stair

import com.perseuspotter.apelles.geo.Point

abstract class Orientation(vertices: Array<Point>) {
    val masterVertices = vertices.map { Point(it.x - 0.5, it.y - 0.5, it.z - 0.5) }
    val vertices = Array(orientations.size) { i ->
        Array(masterVertices.size) {
            val p = masterVertices[it]
            orientations[i].transform(p.x, p.y, p.z)
        }
    }

    companion object {
        interface Rotater {
            fun transform(x: Double, y: Double, z: Double): Point
        }
        // faster than transformation matrices surely
        val orientations = arrayOf(
            object : Rotater { override fun transform(x: Double, y: Double, z: Double): Point = Point(+x, +y, +z) },
            object : Rotater { override fun transform(x: Double, y: Double, z: Double): Point = Point(-x, +y, -z) },
            object : Rotater { override fun transform(x: Double, y: Double, z: Double): Point = Point(-z, +y, +x) },
            object : Rotater { override fun transform(x: Double, y: Double, z: Double): Point = Point(+z, +y, -x) },
            object : Rotater { override fun transform(x: Double, y: Double, z: Double): Point = Point(+x, -y, -z) },
            object : Rotater { override fun transform(x: Double, y: Double, z: Double): Point = Point(-x, -y, +z) },
            object : Rotater { override fun transform(x: Double, y: Double, z: Double): Point = Point(+z, -y, +x) },
            object : Rotater { override fun transform(x: Double, y: Double, z: Double): Point = Point(-z, -y, -x) }
        )
        const val ep = 0.01
    }
}