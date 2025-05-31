package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.Point
import org.lwjgl.opengl.GL11
import kotlin.math.*

object Icosphere : Geometry() {
    override val name = "icosphere"
    val icoVertices = mutableListOf<Array<Point>>()
    val icoTriangles = mutableListOf<Array<IntArray>>()
    val icoStripsD = mutableListOf<DoubleArray>()
    val icoStripsI = mutableListOf<IntArray>()
    init {
        val V_ANGLE = atan(0.5)
        val V_HEIGHT = sin(V_ANGLE)
        val H_LENGTH = cos(V_ANGLE)
        val H_OFFSET = 72 * PI / 180
        val verts = Array(12) {
            when (it) {
                0 -> Point(0.0, 1.0, 0.0)
                11 -> Point(0.0, -1.0, 0.0)
                else -> {
                    val i = (it - 1) % 5
                    val H_ANGLE = (if (it >= 6) i + 0.5 else i.toDouble()) * H_OFFSET
                    Point(
                        H_LENGTH * cos(H_ANGLE),
                        if (it >= 6) -V_HEIGHT else V_HEIGHT,
                        H_LENGTH * sin(H_ANGLE)
                    )
                }
            }
        }
        val tris = arrayOf(
            intArrayOf(0, 1, 5),
            intArrayOf(0, 2, 1),
            intArrayOf(0, 3, 2),
            intArrayOf(0, 4, 3),
            intArrayOf(0, 5, 4),
            intArrayOf(1, 2, 6),
            intArrayOf(2, 7, 6),
            intArrayOf(2, 3, 7),
            intArrayOf(3, 8, 7),
            intArrayOf(3, 4, 8),
            intArrayOf(4, 9, 8),
            intArrayOf(4, 5, 9),
            intArrayOf(5, 10, 9),
            intArrayOf(5, 1, 10),
            intArrayOf(1, 6, 10),
            intArrayOf(11, 6, 7),
            intArrayOf(11, 7, 8),
            intArrayOf(11, 8, 9),
            intArrayOf(11, 9, 10),
            intArrayOf(11, 10, 6)
        )
        var strip = toTriangleStrip(12, tris, true)

        val stripI = IntArray(strip.remaining())
        strip.get(stripI)
        icoStripsI.add(stripI)

        strip = toTriangleStrip(verts.size, tris, false)
        val stripD = DoubleArray(strip.remaining() * 3)
        for (i in 0 until strip.remaining()) {
            val v = strip.get()
            stripD[i * 3 + 0] = verts[v].x
            stripD[i * 3 + 1] = verts[v].y
            stripD[i * 3 + 2] = verts[v].z
        }
        icoStripsD.add(stripD)

        icoVertices.add(verts)
        icoTriangles.add(tris)
    }
    fun checkIcosphere(divisions: Int) {
        if (divisions < 0) throw IllegalArgumentException("no.")
        if (divisions > icoVertices.size) checkIcosphere(divisions - 1)
        if (divisions < icoVertices.size) return

        val prevV = icoVertices[divisions - 1]
        val prevT = icoTriangles[divisions - 1]
        val verts = arrayOfNulls<Point>(prevV.size + prevT.size * 3 / 2)
        prevV.forEachIndexed { i, v -> verts[i] = v }
        var idx = prevV.size
        val vertMap = mutableMapOf<Int, Int>()
        fun getVert(v1: Int, v2: Int): Int {
            val id = if (v1 < v2) v1 * prevV.size + v2 else v2 * prevV.size + v1
            return vertMap.getOrPut(id) {
                val p = slerp(prevV[v1], prevV[v2], 0.5)
                verts[idx] = p
                idx++
            }
        }
        val tris = Array(prevT.size * 4) { IntArray(3) }
        prevT.forEachIndexed { i, v ->
            tris[i * 4 + 0][0] = v[0]
            tris[i * 4 + 0][1] = getVert(v[0], v[1])
            tris[i * 4 + 0][2] = getVert(v[2], v[0])
            tris[i * 4 + 1][0] = getVert(v[0], v[1])
            tris[i * 4 + 1][1] = getVert(v[1], v[2])
            tris[i * 4 + 1][2] = getVert(v[2], v[0])
            tris[i * 4 + 2][0] = v[2]
            tris[i * 4 + 2][1] = getVert(v[2], v[0])
            tris[i * 4 + 2][2] = getVert(v[1], v[2])
            tris[i * 4 + 3][0] = getVert(v[1], v[2])
            tris[i * 4 + 3][1] = getVert(v[0], v[1])
            tris[i * 4 + 3][2] = v[1]
        }
        icoVertices.add(verts as Array<Point>)
        icoTriangles.add(tris)

        var strip = toTriangleStrip(verts.size, tris, true)

        val stripI = IntArray(strip.remaining())
        strip.get(stripI)
        icoStripsI.add(stripI)

        strip = toTriangleStrip(verts.size, tris, false)
        val stripD = DoubleArray(strip.remaining() * 3)
        for (i in 0 until strip.remaining()) {
            val v = strip.get()
            stripD[i * 3 + 0] = verts[v].x
            stripD[i * 3 + 1] = verts[v].y
            stripD[i * 3 + 2] = verts[v].z
        }
        icoStripsD.add(stripD)
    }

    fun slerp(p1: Point, p2: Point, f: Double): Point {
        val dot = p1.x * p2.x + p1.y * p2.y + p1.z * p2.z
        val t = acos(dot)
        if (t < 1e-6) return Point(
            p1.x + f * (p2.x - p1.x),
            p1.y + f * (p2.y - p1.y),
            p1.z + f * (p2.z - p1.z)
        )

        val s = sin(t)
        val w1 = sin((1 - f) * t) / s
        val w2 = sin(f * t) / s

        return Point(
            w1 * p1.x + w2 * p2.x,
            w1 * p1.y + w2 * p2.y,
            w1 * p1.z + w2 * p2.z
        )
    }

    override fun render(pt: Double, params: List<Double>) {
        val (_x, _y, _z, _r, d) = params
        val (x, y, z, s) = rescale(_x, _y, _z)
        val r = _r * s

        checkIcosphere(d.toInt())
        begin(GL11.GL_TRIANGLE_STRIP, false, x, y, z)

        if (Renderer.USE_NEW_SHIT) {
            val verts = icoVertices[d.toInt()]
            val strip = icoStripsI[d.toInt()]
            for (v in verts) addVert(x + v.x * r, y + v.y * r, z + v.z * r)
            for (i in strip) {
                if (i < 0) reset()
                else index(i)
            }
        } else {
            val strip = icoStripsD[d.toInt()]
            for (i in strip.indices step 3) {
                pos(
                    x + strip[i + 0] * r,
                    y + strip[i + 1] * r,
                    z + strip[i + 2] * r
                )
            }
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean {
        val (x, y, z, r) = params
        return false ||
                Frustum.test(x, y, z) ||
                Frustum.test(x - r, y - r, z - r) ||
                Frustum.test(x - r, y - r, z + r) ||
                Frustum.test(x - r, y + r, z - r) ||
                Frustum.test(x - r, y + r, z + r) ||
                Frustum.test(x + r, y - r, z - r) ||
                Frustum.test(x + r, y - r, z + r) ||
                Frustum.test(x + r, y + r, z - r) ||
                Frustum.test(x + r, y + r, z + r)
    }

    override fun getVertexCount(params: List<Double>): Int {
        val d = params[4].toInt()
        checkIcosphere(d)
        return icoVertices[d].size
    }
    override fun getIndicesCount(params: List<Double>): Int {
        val d = params[4].toInt()
        checkIcosphere(d)
        return if (Renderer.USE_NEW_SHIT) icoStripsI[d].size else icoStripsD[d].size / 3
    }
    override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
}