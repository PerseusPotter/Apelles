package com.perseuspotter.apelles.geo

import com.perseuspotter.apelles.Renderer
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import kotlin.math.*

object Geometry3D {
    @JvmField
    val primitive = object : Geometry() {
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

        override fun testPoints(params: List<Double>): Array<Point> = emptyArray()

        override fun getVertexCount(params: List<Double>): Int = params.size / 3
        override fun getIndicesCount(params: List<Double>): Int = params.size / 3
        override fun getDrawMode(params: List<Double>): Int = params[0].toInt()
    }

    @JvmField
    val aabbO = object : Geometry() {
        override val name = "aabbO"
        override fun render(pt: Double, params: List<Double>) {
            val (_x1, _y1, _z1, _x2, _y2, _z2) = params
            val (x1, y1, z1, s) = rescale(_x1, _y1, _z1)
            val x2 = x1 + (_x2 - _x1) * s
            val y2 = y1 + (_y2 - _y1) * s
            val z2 = z1 + (_z2 - _z1) * s

            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_LINE_STRIP, false, (x1 + x2) / 2.0, (y1 + y2) / 2.0, (z1 + z2) / 2.0)
                addVert(x1, y1, z1) // 0
                addVert(x1, y1, z2) // 1
                addVert(x1, y2, z1) // 2
                addVert(x1, y2, z2) // 3
                addVert(x2, y1, z1) // 4
                addVert(x2, y1, z2) // 5
                addVert(x2, y2, z1) // 6
                addVert(x2, y2, z2) // 7

                index(0)
                index(1)
                index(3)
                index(2)
                index(0)
                index(4)
                index(6)
                index(2)
                reset()
                index(1)
                index(5)
                index(7)
                index(3)
                reset()
                index(7)
                index(6)
                reset()
                index(5)
                index(4)
            } else {
                begin(GL11.GL_LINES, false, (x1 + x2) / 2.0, (y1 + y2) / 2.0, (z1 + z2) / 2.0)
                pos(x1, y1, z1); pos(x2, y1, z1)
                pos(x1, y2, z1); pos(x2, y2, z1)
                pos(x1, y1, z1); pos(x1, y2, z1)
                pos(x2, y1, z1); pos(x2, y2, z1)
                pos(x1, y1, z2); pos(x2, y1, z2)
                pos(x1, y2, z2); pos(x2, y2, z2)
                pos(x1, y1, z2); pos(x1, y2, z2)
                pos(x2, y1, z2); pos(x2, y2, z2)
                pos(x1, y1, z1); pos(x1, y1, z2)
                pos(x1, y2, z1); pos(x1, y2, z2)
                pos(x2, y1, z1); pos(x2, y1, z2)
                pos(x2, y2, z1); pos(x2, y2, z2)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x1, y1, z1, x2, y2, z2) = params
            return arrayOf(
                Point(x1, y1, z1),
                Point(x1, y1, z2),
                Point(x1, y2, z1),
                Point(x1, y2, z2),
                Point(x2, y1, z1),
                Point(x2, y1, z2),
                Point(x2, y2, z1),
                Point(x2, y2, z2)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 8
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 19 else 24
        override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_LINE_STRIP else GL11.GL_LINES
    }

    @JvmField
    val aabbF = object : Geometry() {
        override val name = "aabbF"
        override fun render(pt: Double, params: List<Double>) {
            val (_x1, _y1, _z1, _x2, _y2, _z2) = params
            val (x1, y1, z1, s) = rescale(_x1, _y1, _z1)
            val x2 = x1 + (_x2 - _x1) * s
            val y2 = y1 + (_y2 - _y1) * s
            val z2 = z1 + (_z2 - _z1) * s

            begin(GL11.GL_TRIANGLE_STRIP, false, (x1 + x2) / 2.0, (y1 + y2) / 2.0, (z1 + z2) / 2.0)
            if (Renderer.USE_NEW_SHIT) {
                addVert(x1, y1, z1) // 0
                addVert(x1, y1, z2) // 1
                addVert(x1, y2, z1) // 2
                addVert(x1, y2, z2) // 3
                addVert(x2, y1, z1) // 4
                addVert(x2, y1, z2) // 5
                addVert(x2, y2, z1) // 6
                addVert(x2, y2, z2) // 7

                index(0)
                index(2)
                index(4)
                index(6)

                index(5)
                index(7)

                index(1)
                index(3)

                index(0)
                index(2)

                reset()
                index(2)
                index(3)
                index(6)
                index(7)

                reset()
                index(1)
                index(0)
                index(5)
                index(4)
            } else {
                pos(x1, y1, z1)
                pos(x1, y2, z1)
                pos(x2, y1, z1)
                pos(x2, y2, z1)

                pos(x2, y1, z2)
                pos(x2, y2, z2)

                pos(x1, y1, z2)
                pos(x1, y2, z2)

                pos(x1, y1, z1)
                pos(x1, y2, z1)

                pos(x1, y2, z1)

                pos(x1, y2, z2)
                pos(x2, y2, z1)
                pos(x2, y2, z2)

                pos(x2, y2, z2)
                pos(x1, y1, z2)

                pos(x1, y1, z2)
                pos(x1, y1, z1)
                pos(x2, y1, z2)
                pos(x2, y1, z1)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x1, y1, z1, x2, y2, z2) = params
            return arrayOf(
                Point(x1, y1, z1),
                Point(x1, y1, z2),
                Point(x1, y2, z1),
                Point(x1, y2, z2),
                Point(x2, y1, z1),
                Point(x2, y1, z2),
                Point(x2, y2, z1),
                Point(x2, y2, z2)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 8
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 20 else 20
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
    }

    @JvmField
    val beaconI = object : Geometry() {
        override val name = "beaconI"
        override fun render(pt: Double, params: List<Double>) {
            val (x, y1, z, h, s) = params

            val time = 0.2 * ((Minecraft.getMinecraft()?.theWorld?.totalWorldTime ?: 0L) + pt)
            val v0 = ceil(time) - time - 1
            val v1 = h * 2.5 + v0
            val t = time * -0.1875
            val d0 = cos(t + Math.PI * 1 / 4) * 0.2 * s
            val d1 = sin(t + Math.PI * 1 / 4) * 0.2 * s
            val d2 = cos(t + Math.PI * 3 / 4) * 0.2 * s
            // val d3 = sin(t1 + Math.PI * 3 / 4) * 0.2 * s
            val d3 = d0
            val d4 = cos(t + Math.PI * 5 / 4) * 0.2 * s
            // val d5 = sin(t1 + Math.PI * 5 / 4) * 0.2 * s
            val d5 = d2
            // val d6 = cos(t + Math.PI * 7 / 4) * 0.2 * s
            val d6 = d1
            // val d7 = sin(t1 + Math.PI * 7 / 4) * 0.2 * s
            val d7 = d4
            val y2 = y1 + h

            begin(GL11.GL_TRIANGLE_STRIP, true, x, y1 + h / 2.0, z)
            pos(x + d0, y1, z + d1, 0.0, v0)
            pos(x + d0, y2, z + d1, 0.0, v1)
            pos(x + d2, y1, z + d3, 1.0, v0)
            pos(x + d2, y2, z + d3, 1.0, v1)

            pos(x + d4, y1, z + d5, 2.0, v0)
            pos(x + d4, y2, z + d5, 2.0, v1)

            pos(x + d6, y1, z + d7, 3.0, v0)
            pos(x + d6, y2, z + d7, 3.0, v1)

            pos(x + d0, y1, z + d1, 4.0, v0)
            pos(x + d0, y2, z + d1, 4.0, v1)
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> = emptyArray()

        override fun getVertexCount(params: List<Double>): Int = 10
        override fun getIndicesCount(params: List<Double>): Int = 10
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
    }

    @JvmField
    val beaconO = object : Geometry() {
        override val name = "beaconO"
        override fun render(pt: Double, params: List<Double>) {
            val (x, y1, z, h, s) = params

            val time = 0.2 * ((Minecraft.getMinecraft()?.theWorld?.totalWorldTime ?: 0L) + pt)
            val v0 = ceil(time) - time - 1
            val v1 = h + v0
            val x1 = x - 0.3 * s
            val x2 = x + 0.3 * s
            val z1 = z - 0.3 * s
            val z2 = z + 0.3 * s
            val y2 = y1 + h

            begin(GL11.GL_TRIANGLE_STRIP, true, x, y1 + h / 2.0, z)
            pos(x1, y1, z1, 0.0, v0)
            pos(x1, y2, z1, 0.0, v1)
            pos(x2, y1, z1, 1.0, v0)
            pos(x2, y2, z1, 1.0, v1)

            pos(x2, y1, z2, 2.0, v0)
            pos(x2, y2, z2, 2.0, v1)

            pos(x1, y1, z2, 3.0, v0)
            pos(x1, y2, z2, 3.0, v1)

            pos(x1, y1, z1, 4.0, v0)
            pos(x1, y2, z1, 4.0, v1)
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> = emptyArray()

        override fun getVertexCount(params: List<Double>): Int = 10
        override fun getIndicesCount(params: List<Double>): Int = 10
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
    }

    @JvmField
    val beaconTI = object : Geometry() {
        override val name = "beaconTI"
        override fun render(pt: Double, params: List<Double>) {
            val (x, y1, z, h, s) = params

            val time = 0.2 * ((Minecraft.getMinecraft()?.theWorld?.totalWorldTime ?: 0L) + pt)
            val t = time * -0.1875
            val d0 = cos(t + Math.PI * 1 / 4) * 0.2 * s
            val d1 = sin(t + Math.PI * 1 / 4) * 0.2 * s
            val d2 = cos(t + Math.PI * 3 / 4) * 0.2 * s
            // val d3 = sin(t1 + Math.PI * 3 / 4) * 0.2 * s
            val d3 = d0
            val d4 = cos(t + Math.PI * 5 / 4) * 0.2 * s
            // val d5 = sin(t1 + Math.PI * 5 / 4) * 0.2 * s
            val d5 = d2
            // val d6 = cos(t + Math.PI * 7 / 4) * 0.2 * s
            val d6 = d1
            // val d7 = sin(t1 + Math.PI * 7 / 4) * 0.2 * s
            val d7 = d4
            val y2 = y1 + h

            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_TRIANGLE_STRIP, false, x, y1 + h / 2.0, z)
                pos(x + d0, y1, z + d1)
                pos(x + d2, y1, z + d3)
                pos(x + d6, y1, z + d7)
                pos(x + d4, y1, z + d5)

                reset()
                pos(x + d6, y2, z + d7)
                pos(x + d4, y2, z + d5)
                pos(x + d0, y2, z + d1)
                pos(x + d2, y2, z + d3)
            } else {
                begin(GL11.GL_QUADS, false, x, y1 + h / 2.0, z)
                pos(x + d0, y1, z + d1)
                pos(x + d2, y1, z + d3)
                pos(x + d4, y1, z + d5)
                pos(x + d6, y1, z + d7)

                pos(x + d6, y2, z + d7)
                pos(x + d4, y2, z + d5)
                pos(x + d2, y2, z + d3)
                pos(x + d0, y2, z + d1)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> = emptyArray()

        override fun getVertexCount(params: List<Double>): Int = 8
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 9 else 8
        override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_TRIANGLE_STRIP else GL11.GL_QUADS
    }

    @JvmField
    val beaconTO = object : Geometry() {
        override val name = "beaconTO"
        override fun render(pt: Double, params: List<Double>) {
            val (x, y1, z, h, s) = params

            val x1 = x - 0.3 * s
            val x2 = x + 0.3 * s
            val z1 = z - 0.3 * s
            val z2 = z + 0.3 * s
            val y2 = y1 + h

            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_TRIANGLE_STRIP, false, x, y1 + h / 2.0, z)
                pos(x1, y1, z1)
                pos(x2, y1, z1)
                pos(x1, y1, z2)
                pos(x2, y1, z2)

                reset()
                pos(x1, y2, z2)
                pos(x2, y2, z2)
                pos(x1, y2, z1)
                pos(x2, y2, z1)
            } else {
                begin(GL11.GL_QUADS, false, x, y1 + h / 2.0, z)
                pos(x1, y1, z1)
                pos(x2, y1, z1)
                pos(x2, y1, z2)
                pos(x1, y1, z2)

                pos(x1, y2, z2)
                pos(x2, y2, z2)
                pos(x2, y2, z1)
                pos(x1, y2, z1)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> = emptyArray()

        override fun getVertexCount(params: List<Double>): Int = 8
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 9 else 8
        override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_TRIANGLE_STRIP else GL11.GL_QUADS
    }

    @JvmField
    val icosphere = object : Geometry() {
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
            val strip = toTriangleStrip(12, tris, Renderer.USE_NEW_SHIT)

            val stripI = IntArray(strip.remaining())
            strip.get(stripI)
            icoStripsI.add(stripI)

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

            val strip = toTriangleStrip(verts.size, tris, Renderer.USE_NEW_SHIT)

            val stripI = IntArray(strip.remaining())
            strip.get(stripI)
            icoStripsI.add(stripI)

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

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z, r) = params
            return arrayOf(
                Point(x, y, z),
                Point(x - r, y - r, z - r),
                Point(x - r, y - r, z + r),
                Point(x - r, y + r, z - r),
                Point(x - r, y + r, z + r),
                Point(x + r, y - r, z - r),
                Point(x + r, y - r, z + r),
                Point(x + r, y + r, z - r),
                Point(x + r, y + r, z + r)
            )
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

    @JvmField
    val pyramidO = object : Geometry() {
        override val name = "pyramidO"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _r, _h, _n) = params
            val (x, y, z, s) = rescale(_x, _y, _z)
            val r = _r * s
            val h = _h * s
            val n = _n.toInt()

            val a0 = if (n == 4) PI / 4.0 else 0.0
            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_LINE_STRIP, false, x, y + h / 4.0, z)
                addVert(x, y + h, z)
                for (i in 0 until n) {
                    val a = a0 + 2.0 * PI * i / n
                    addVert(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                }

                for (i in 1..n) index(i)
                index(1)
                for (i in 1 until n step 2) {
                    reset()
                    index(i)
                    index(0)
                    index(i + 1)
                }
                if (n and 1 == 1) {
                    reset()
                    index(n)
                    index(0)
                }
            } else {
                begin(GL11.GL_LINES, false, x, y + h / 4.0, z)

                for (i in 0 until n) {
                    val a = a0 + 2.0 * PI * i / n
                    if (i > 0) pos(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                    pos(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                    pos(x, y + h, z)
                    pos(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                }
                pos(
                    x + cos(a0) * r,
                    y,
                    z + sin(a0) * r
                )
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z, r, h) = params
            return arrayOf(
                Point(x, y + h, z),
                Point(x + r, y, z + r),
                Point(x + r, y, z - r),
                Point(x - r, y, z + r),
                Point(x - r, y, z - r)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 1 + params[5].toInt()
        override fun getIndicesCount(params: List<Double>): Int {
            val n = params[5].toInt()
            return if (Renderer.USE_NEW_SHIT) 3 * n + 1 + (n and 1) else 4 * n
        }
        override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_LINE_STRIP else GL11.GL_LINES
    }

    @JvmField
    val pyramidF = object : Geometry() {
        override val name = "pyramidF"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _r, _h, _n) = params
            val (x, y, z, s) = rescale(_x, _y, _z)
            val r = _r * s
            val h = _h * s
            val n = _n.toInt()

            val a0 = if (n == 4) PI / 4.0 else 0.0

            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 4.0, z)
                addVert(x, y, z)
                addVert(x, y + h, z)
                for (i in 0 until n) {
                    val a = a0 + 2.0 * PI * (if (h > 0) n - i else i) / n
                    addVert(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                }

                index(1)
                for (i in 2..n + 1) index(i)
                index(2)
                reset()
                index(0)
                for (i in n + 1 downTo 2) index(i)
                index(n + 1)
                draw()
            } else {
                begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 4.0, z)
                pos(x, y + h, z)

                for (i in 0 until n) {
                    val a = a0 + 2.0 * PI * (if (h > 0) n - i else i) / n
                    pos(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                }
                pos(
                    x + cos(a0) * r,
                    y,
                    z + sin(a0) * r
                )
                draw()

                begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 4.0, z)
                pos(x, y, z)

                for (i in 0 until n) {
                    val a = a0 + 2.0 * PI * (if (h > 0) i else n - i) / n
                    pos(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                }
                pos(
                    x + cos(a0) * r,
                    y,
                    z + sin(a0) * r
                )
                draw()
            }
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z, r, h) = params
            return arrayOf(
                Point(x, y + h, z),
                Point(x + r, y, z + r),
                Point(x + r, y, z - r),
                Point(x - r, y, z + r),
                Point(x - r, y, z - r)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 2 + params[5].toInt()
        override fun getIndicesCount(params: List<Double>): Int = 2 * (params[5].toInt() + 2) + if (Renderer.USE_NEW_SHIT) 1 else 0
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_FAN
    }

    @JvmField
    val vertCylinderR = object : Geometry() {
        override val name = "vertCylinderR"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _r, _h, _n) = params
            val (x, y, z, s) = rescale(_x, _y, _z)
            val r = _r * s
            val h = _h * s
            val n = _n.toInt()

            begin(GL11.GL_TRIANGLE_STRIP, false, x, y + h / 2.0, z)
            if (Renderer.USE_NEW_SHIT) {
                addVert(x + r, y, z)
                addVert(x + r, y + h, z)
                for (i in 1 until n) {
                    val a = 2.0 * PI * i / n
                    addVert(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                    addVert(
                        x + cos(a) * r,
                        y + h,
                        z + sin(a) * r
                    )
                }

                for (i in 0 until 2 * n) index(i)
                index(0)
                index(1)
            } else {
                pos(
                    x + r,
                    y,
                    z
                )
                pos(
                    x + r,
                    y + h,
                    z
                )
                for (i in 1 until n) {
                    val a = 2.0 * PI * i / n
                    pos(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                    pos(
                        x + cos(a) * r,
                        y + h,
                        z + sin(a) * r
                    )
                }
                pos(
                    x + r,
                    y,
                    z
                )
                pos(
                    x + r,
                    y + h,
                    z
                )
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z, r, h) = params
            return arrayOf(
                Point(x, y + h, z),
                Point(x + r, y, z + r),
                Point(x + r, y, z - r),
                Point(x - r, y, z + r),
                Point(x - r, y, z - r)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 2 * params[5].toInt()
        override fun getIndicesCount(params: List<Double>): Int = 2 * (params[5].toInt() + 1)
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
    }

    @JvmField
    val vertCylinderC = object : Geometry() {
        override val name = "vertCylinderC"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _r, _h, _n) = params
            val (x, y, z, s) = rescale(_x, _y, _z)
            val r = _r * s
            val h = _h * s
            val n = _n.toInt()

            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 2.0, z)
                addVert(x, y, z)
                addVert(x + r, y, z)
                for (i in 1 until n) {
                    val a = 2.0 * PI * i / n
                    addVert(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                }
                addVert(x, y + h, z)
                addVert(x + r, y + h, z)
                for (i in 1 until n) {
                    val a = 2.0 * PI * i / n
                    addVert(
                        x + cos(a) * r,
                        y + h,
                        z + sin(a) * r
                    )
                }

                index(0)
                for (i in 1..n) index(i)
                index(1)
                reset()
                index(n + 1)
                index(n + 2)
                for (i in 2 * n + 1 downTo n + 2) index(i)
                draw()
            } else {
                begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 2.0, z)
                pos(x, y + h, z)

                pos(
                    x + r,
                    y + h,
                    z
                )
                for (i in 1 until n) {
                    val a = 2.0 * PI * (n - i) / n
                    pos(
                        x + cos(a) * r,
                        y + h,
                        z + sin(a) * r
                    )
                }
                pos(
                    x + r,
                    y + h,
                    z
                )
                draw()

                begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 2.0, z)
                pos(x, y, z)

                pos(
                    x + r,
                    y,
                    z
                )
                for (i in 1 until n) {
                    val a = 2.0 * PI * i / n
                    pos(
                        x + cos(a) * r,
                        y,
                        z + sin(a) * r
                    )
                }
                pos(
                    x + r,
                    y,
                    z
                )
                draw()
            }
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z, r, h) = params
            return arrayOf(
                Point(x, y + h, z),
                Point(x + r, y, z + r),
                Point(x + r, y, z - r),
                Point(x - r, y, z + r),
                Point(x - r, y, z - r)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 2 * (params[5].toInt() + 1)
        override fun getIndicesCount(params: List<Double>): Int = 2 * (params[5].toInt() + 2) + if (Renderer.USE_NEW_SHIT) 1 else 0
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_FAN
    }

    @JvmField
    val octahedronO = object : Geometry() {
        override val name = "octahedronO"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _w, _h) = params
            val (x, y, z, s) = rescale(_x, _y, _z)
            val w = _w * s
            val h = _h * s

            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_LINE_STRIP, false, x, y, z)
                addVert(x, y + h, z)
                addVert(x + w, y, z - w)
                addVert(x - w, y, z - w)
                addVert(x - w, y, z + w)
                addVert(x + w, y, z + w)
                addVert(x, y - h, z)

                index(0)
                index(1)
                index(2)
                index(0)
                index(3)
                index(4)
                index(0)
                reset()
                index(5)
                index(2)
                index(3)
                index(5)
                index(4)
                index(1)
                index(5)
            } else {
                begin(GL11.GL_LINES, false, x, y, z)
                pos(x, y + h, z); pos(x + w, y, z + w)
                pos(x, y + h, z); pos(x + w, y, z - w)
                pos(x, y + h, z); pos(x - w, y, z + w)
                pos(x, y + h, z); pos(x - w, y, z - w)

                pos(x + w, y, z + w); pos(x + w, y, z - w)
                pos(x + w, y, z - w); pos(x - w, y, z - w)
                pos(x - w, y, z - w); pos(x - w, y, z + w)
                pos(x - w, y, z + w); pos(x + w, y, z + w)

                pos(x, y - h, z); pos(x + w, y, z + w)
                pos(x, y - h, z); pos(x + w, y, z - w)
                pos(x, y - h, z); pos(x - w, y, z + w)
                pos(x, y - h, z); pos(x - w, y, z - w)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z, w, h) = params
            return arrayOf(
                Point(x, y + h, z),
                Point(x + w, y, z + w),
                Point(x + w, y, z - w),
                Point(x - w, y, z + w),
                Point(x - w, y, z - w),
                Point(x, y - h, z)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 6
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 15 else 24
        override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_LINE_STRIP else GL11.GL_LINES
    }

    @JvmField
    val octahedronF = object : Geometry() {
        override val name = "octahedronF"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _w, _h) = params
            val (x, y, z, s) = rescale(_x, _y, _z)
            val w = _w * s
            val h = _h * s

            begin(GL11.GL_TRIANGLE_STRIP, false, x, y, z)
            if (Renderer.USE_NEW_SHIT) {
                addVert(x, y + h, z)
                addVert(x + w, y, z - w)
                addVert(x - w, y, z - w)
                addVert(x - w, y, z + w)
                addVert(x + w, y, z + w)
                addVert(x, y - h, z)

                index(0)
                index(1)
                index(2)
                index(5)
                index(3)
                index(4)
                index(0)
                index(1)
                reset()
                index(1)
                index(4)
                index(5)
                reset()
                index(0)
                index(2)
                index(3)
            } else {
                pos(x, y + h, z)
                pos(x + w, y, z - w)
                pos(x - w, y, z - w)
                pos(x, y - h, z)
                pos(x - w, y, z + w)
                pos(x + w, y, z + w)
                pos(x, y + h, z)
                pos(x + w, y, z - w)
                pos(x + w, y, z - w)
                pos(x + w, y, z + w)
                pos(x, y - h, z)
                pos(x, y - h, z)
                pos(x, y + h, z)
                pos(x, y + h, z)
                pos(x - w, y, z + w)
                pos(x - w, y, z - w)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z, w, h) = params
            return arrayOf(
                Point(x, y + h, z),
                Point(x + w, y, z + w),
                Point(x + w, y, z - w),
                Point(x - w, y, z + w),
                Point(x - w, y, z - w),
                Point(x, y - h, z)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 6
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 16 else 16
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
    }

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
    val stairStraightVertices: Array<Array<Point>>
    val stairInnerVertices: Array<Array<Point>>
    val stairOuterVertices: Array<Array<Point>>
    init {
        val ep = 0.01
        val straightVertices = arrayOf(
            Point(0.75, 0.25, 0.5),
            // 0
            Point(0.0 - ep, 0.0 - ep, 0.0 - ep),
            Point(0.0 - ep, 0.0 - ep, 1.0 + ep),
            Point(1.0 + ep, 0.0 - ep, 0.0 - ep),
            Point(1.0 + ep, 0.0 - ep, 1.0 + ep),
            // 4
            Point(0.0 - ep, 0.5 + ep, 0.0 - ep),
            Point(0.0 - ep, 0.5 + ep, 1.0 + ep),
            // 6
            Point(0.5 - ep, 0.5 + ep, 0.0 - ep),
            Point(0.5 - ep, 0.5 + ep, 1.0 + ep),
            // 8
            Point(0.5 - ep, 1.0 + ep, 0.0 - ep),
            Point(0.5 - ep, 1.0 + ep, 1.0 + ep),
            // 10
            Point(1.0 + ep, 1.0 + ep, 0.0 - ep),
            Point(1.0 + ep, 1.0 + ep, 1.0 + ep)
        ).map { Point(it.x - 0.5, it.y - 0.5, it.z - 0.5) }
        val innerVertices = arrayOf(
            Point(0.75, 0.25, 0.75),
            // 0
            Point(0.0 - ep, 0.0 - ep, 0.0 - ep),
            Point(0.0 - ep, 0.0 - ep, 1.0 + ep),
            Point(1.0 + ep, 0.0 - ep, 0.0 - ep),
            Point(1.0 + ep, 0.0 - ep, 1.0 + ep),
            // 4
            Point(0.0 - ep, 0.5 + ep, 0.0 - ep),
            Point(0.0 - ep, 0.5 + ep, 0.5 - ep),
            Point(0.5 - ep, 0.5 + ep, 0.0 - ep),
            Point(0.5 - ep, 0.5 + ep, 0.5 - ep),
            // 8
            Point(0.0 - ep, 1.0 + ep, 0.5 - ep),
            Point(0.0 - ep, 1.0 + ep, 1.0 + ep),
            Point(0.5 - ep, 1.0 + ep, 0.0 - ep),
            Point(0.5 - ep, 1.0 + ep, 0.5 - ep),
            Point(1.0 + ep, 1.0 + ep, 0.0 - ep),
            Point(1.0 + ep, 1.0 + ep, 1.0 + ep)
        ).map { Point(it.x - 0.5, it.y - 0.5, it.z - 0.5) }
        val outerVertices = arrayOf(
            Point(0.25, 0.25, 0.25),
            // 0
            Point(0.0 - ep, 0.0 - ep, 0.0 - ep),
            Point(0.0 - ep, 0.0 - ep, 1.0 + ep),
            Point(1.0 + ep, 0.0 - ep, 0.0 - ep),
            Point(1.0 + ep, 0.0 - ep, 1.0 + ep),
            // 4
            Point(0.0 - ep, 0.5 + ep, 0.0 - ep),
            Point(0.0 - ep, 0.5 + ep, 1.0 + ep),
            Point(0.5 - ep, 0.5 + ep, 0.5 - ep),
            Point(0.5 - ep, 0.5 + ep, 1.0 + ep),
            Point(1.0 + ep, 0.5 + ep, 0.0 - ep),
            Point(1.0 + ep, 0.5 + ep, 0.5 - ep),
            // 10
            Point(0.5 - ep, 1.0 + ep, 0.5 - ep),
            Point(0.5 - ep, 1.0 + ep, 1.0 + ep),
            Point(1.0 + ep, 1.0 + ep, 0.5 - ep),
            Point(1.0 + ep, 1.0 + ep, 1.0 + ep)
        ).map { Point(it.x - 0.5, it.y - 0.5, it.z - 0.5) }
        stairStraightVertices = Array(orientations.size) { i ->
            Array(straightVertices.size) {
                val p = straightVertices[it]
                orientations[i].transform(p.x, p.y, p.z)
            }
        }
        stairInnerVertices = Array(orientations.size) { i ->
            Array(innerVertices.size) {
                val p = innerVertices[it]
                orientations[i].transform(p.x, p.y, p.z)
            }
        }
        stairOuterVertices = Array(orientations.size) { i ->
            Array(outerVertices.size) {
                val p = outerVertices[it]
                orientations[i].transform(p.x, p.y, p.z)
            }
        }
    }

    val stairStraightO = object : Geometry() {
        override val name: String = "stairStraightO"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _c) = params
            val (x, y, z, s) = rescale(_x, _y , _z)
            val verts = stairStraightVertices[_c.toInt()]

            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_LINE_STRIP, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
                verts.forEachIndexed { i, v -> if (i > 0) addVert(x + v.x * s, y + v.y * s, z + v.z * s) }

                index(0)
                index(1)
                index(5)
                index(4)
                index(0)
                index(2)
                index(10)
                index(8)
                index(6)
                index(4)
                reset()
                index(1)
                index(3)
                index(11)
                index(9)
                index(7)
                index(5)
                reset()
                index(6)
                index(7)
                reset()
                index(8)
                index(9)
                reset()
                index(10)
                index(11)
                reset()
                index(2)
                index(3)
                reset()
            } else {
                begin(GL11.GL_LINES, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
                fun emit(i: Int) = pos(x + verts[i + 1].x * s, y + verts[i + 1].y * s, z + verts[i + 1].z * s)

                emit(0); emit(1)
                emit(1); emit(5)
                emit(5); emit(4)
                emit(4); emit(0)
                emit(0); emit(2)
                emit(2); emit(10)
                emit(10); emit(8)
                emit(8); emit(6)
                emit(6); emit(4)

                emit(1); emit(3)
                emit(3); emit(11)
                emit(11); emit(9)
                emit(9); emit(7)
                emit(7); emit(5)

                emit(6); emit(7)
                emit(8); emit(9)
                emit(10); emit(11)
                emit(2); emit(3)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z) = params
            return arrayOf(
                Point(x + 0.5, y + 0.5, z + 0.5),
                Point(x + 0.5, y + 0.5, z - 0.5),
                Point(x + 0.5, y - 0.5, z + 0.5),
                Point(x + 0.5, y - 0.5, z - 0.5),
                Point(x - 0.5, y + 0.5, z + 0.5),
                Point(x - 0.5, y + 0.5, z - 0.5),
                Point(x - 0.5, y - 0.5, z + 0.5),
                Point(x - 0.5, y - 0.5, z - 0.5)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 12
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 30 else 36
        override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_LINE_STRIP else GL11.GL_LINES
    }

    val stairStraightF = object : Geometry() {
        override val name: String = "stairStraightF"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _c) = params
            val (x, y, z, s) = rescale(_x, _y , _z)
            val verts = stairStraightVertices[_c.toInt()]

            begin(GL11.GL_TRIANGLE_STRIP, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
            if (Renderer.USE_NEW_SHIT) {
                verts.forEachIndexed { i, v -> if (i > 0) addVert(x + v.x * s, y + v.y * s, z + v.z * s) }

                index(0)
                index(1)
                index(4)
                index(5)
                index(6)
                index(7)
                index(8)
                index(9)
                index(10)
                index(11)
                index(2)
                index(3)
                index(0)
                index(1)

                reset()
                index(10)
                index(10)
                index(8)
                index(2)
                index(6)
                index(0)
                index(4)

                reset()
                index(11)
                index(9)
                index(3)
                index(7)
                index(1)
                index(5)
            } else {
                fun emit(i: Int) = pos(x + verts[i + 1].x * s, y + verts[i + 1].y * s, z + verts[i + 1].z * s)

                emit(0)
                emit(1)
                emit(4)
                emit(5)
                emit(6)
                emit(7)
                emit(8)
                emit(9)
                emit(10)
                emit(11)
                emit(2)
                emit(3)
                emit(0)
                emit(1)

                emit(1)
                emit(10)
                emit(10)
                emit(10)
                emit(8)
                emit(2)
                emit(6)
                emit(0)
                emit(4)

                emit(4)
                emit(11)
                emit(11)
                emit(11)
                emit(9)
                emit(3)
                emit(7)
                emit(1)
                emit(5)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z) = params
            return arrayOf(
                Point(x + 0.5, y + 0.5, z + 0.5),
                Point(x + 0.5, y + 0.5, z - 0.5),
                Point(x + 0.5, y - 0.5, z + 0.5),
                Point(x + 0.5, y - 0.5, z - 0.5),
                Point(x - 0.5, y + 0.5, z + 0.5),
                Point(x - 0.5, y + 0.5, z - 0.5),
                Point(x - 0.5, y - 0.5, z + 0.5),
                Point(x - 0.5, y - 0.5, z - 0.5)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 12
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 29 else 32
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
    }

    val stairInnerO = object : Geometry() {
        override val name: String = "stairInnerO"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _c) = params
            val (x, y, z, s) = rescale(_x, _y , _z)
            val verts = stairInnerVertices[_c.toInt()]

            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_LINE_STRIP, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
                verts.forEachIndexed { i, v -> if (i > 0) addVert(x + v.x * s, y + v.y * s, z + v.z * s) }

                index(0)
                index(1)
                index(3)
                index(2)
                index(0)
                index(4)
                index(5)
                index(8)
                index(9)
                index(1)
                reset()
                index(4)
                index(6)
                index(7)
                index(5)
                reset()
                index(6)
                index(10)
                index(11)
                index(7)
                reset()
                index(11)
                index(8)
                reset()
                index(9)
                index(13)
                index(12)
                index(2)
                reset()
                index(3)
                index(13)
                reset()
                index(10)
                index(12)
            } else {
                begin(GL11.GL_LINES, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
                fun emit(i: Int) = pos(x + verts[i + 1].x * s, y + verts[i + 1].y * s, z + verts[i + 1].z * s)

                emit(0); emit(1)
                emit(1); emit(3)
                emit(3); emit(2)
                emit(2); emit(0)
                emit(0); emit(4)
                emit(4); emit(5)
                emit(5); emit(8)
                emit(8); emit(9)
                emit(9); emit(1)

                emit(4); emit(6)
                emit(6); emit(7)
                emit(7); emit(5)

                emit(6); emit(10)
                emit(10); emit(11)
                emit(11); emit(7)

                emit(11); emit(8)

                emit(9); emit(13)
                emit(13); emit(12)
                emit(12); emit(2)

                emit(3); emit(13)

                emit(10); emit(12)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z) = params
            return arrayOf(
                Point(x + 0.5, y + 0.5, z + 0.5),
                Point(x + 0.5, y + 0.5, z - 0.5),
                Point(x + 0.5, y - 0.5, z + 0.5),
                Point(x + 0.5, y - 0.5, z - 0.5),
                Point(x - 0.5, y + 0.5, z + 0.5),
                Point(x - 0.5, y + 0.5, z - 0.5),
                Point(x - 0.5, y - 0.5, z + 0.5),
                Point(x - 0.5, y - 0.5, z - 0.5)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 14
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 34 else 42
        override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_LINE_STRIP else GL11.GL_LINES
    }

    val stairInnerF = object : Geometry() {
        override val name: String = "stairInnerF"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _c) = params
            val (x, y, z, s) = rescale(_x, _y , _z)
            val verts = stairInnerVertices[_c.toInt()]

            begin(GL11.GL_TRIANGLE_STRIP, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
            if (Renderer.USE_NEW_SHIT) {
                verts.forEachIndexed { i, v -> if (i > 0) addVert(x + v.x * s, y + v.y * s, z + v.z * s) }

                index(12)
                index(12)
                index(10)
                index(2)
                index(6)
                index(0)
                index(4)
                index(1)
                index(5)
                index(9)
                index(8)
                index(13)
                index(11)
                index(12)
                index(10)

                reset()
                index(11)
                index(10)
                index(7)
                index(6)
                index(5)
                index(4)
                reset()
                index(8)
                index(11)
                index(5)
                index(7)

                reset()
                index(12)
                index(13)
                index(2)
                index(3)
                index(0)
                index(1)
                reset()
                index(13)
                index(9)
                index(3)
                index(1)
            } else {
                fun emit(i: Int) = pos(x + verts[i + 1].x * s, y + verts[i + 1].y * s, z + verts[i + 1].z * s)

                emit(12)
                emit(12)
                emit(10)
                emit(2)
                emit(6)
                emit(0)
                emit(4)
                emit(1)
                emit(5)
                emit(9)
                emit(8)
                emit(13)
                emit(11)
                emit(12)
                emit(10)

                emit(10)
                emit(11)
                emit(6)
                emit(7)
                emit(4)
                emit(5)
                emit(5)
                emit(7)
                emit(8)
                emit(11)

                emit(11)
                emit(9)
                emit(9)
                emit(13)
                emit(1)
                emit(3)
                emit(0)
                emit(2)
                emit(2)
                emit(3)
                emit(12)
                emit(13)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z) = params
            return arrayOf(
                Point(x + 0.5, y + 0.5, z + 0.5),
                Point(x + 0.5, y + 0.5, z - 0.5),
                Point(x + 0.5, y - 0.5, z + 0.5),
                Point(x + 0.5, y - 0.5, z - 0.5),
                Point(x - 0.5, y + 0.5, z + 0.5),
                Point(x - 0.5, y + 0.5, z - 0.5),
                Point(x - 0.5, y - 0.5, z + 0.5),
                Point(x - 0.5, y - 0.5, z - 0.5)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 14
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 39 else 37
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
    }

    val stairOuterO = object : Geometry() {
        override val name: String = "stairOuterO"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _c) = params
            val (x, y, z, s) = rescale(_x, _y , _z)
            val verts = stairOuterVertices[_c.toInt()]

            if (Renderer.USE_NEW_SHIT) {
                begin(GL11.GL_LINE_STRIP, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
                verts.forEachIndexed { i, v -> if (i > 0) addVert(x + v.x * s, y + v.y * s, z + v.z * s) }

                index(0)
                index(1)
                index(5)
                reset()
                index(1)
                index(3)
                index(13)
                reset()
                index(3)
                index(2)
                index(8)
                reset()
                index(2)
                index(0)
                index(4)

                index(5)
                index(7)
                index(6)
                index(9)
                index(8)
                index(4)

                reset()
                index(7)
                index(11)
                index(10)
                index(6)
                reset()
                index(11)
                index(13)
                index(12)
                index(9)

                reset()
                index(10)
                index(12)
            } else {
                begin(GL11.GL_LINES, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
                fun emit(i: Int) = pos(x + verts[i + 1].x * s, y + verts[i + 1].y * s, z + verts[i + 1].z * s)

                emit(0); emit(1)
                emit(1); emit(5)
                emit(1); emit(3)
                emit(3); emit(13)
                emit(3); emit(2)
                emit(2); emit(8)
                emit(2); emit(0)
                emit(0); emit(4)

                emit(4); emit(5)
                emit(5); emit(7)
                emit(7); emit(6)
                emit(6); emit(9)
                emit(9); emit(8)
                emit(8); emit(4)

                emit(7); emit(11)
                emit(11); emit(10)
                emit(10); emit(6)
                emit(11); emit(13)
                emit(13); emit(12)
                emit(12); emit(9)

                emit(10); emit(12)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z) = params
            return arrayOf(
                Point(x + 0.5, y + 0.5, z + 0.5),
                Point(x + 0.5, y + 0.5, z - 0.5),
                Point(x + 0.5, y - 0.5, z + 0.5),
                Point(x + 0.5, y - 0.5, z - 0.5),
                Point(x - 0.5, y + 0.5, z + 0.5),
                Point(x - 0.5, y + 0.5, z - 0.5),
                Point(x - 0.5, y - 0.5, z + 0.5),
                Point(x - 0.5, y - 0.5, z - 0.5)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 14
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 34 else 42
        override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_LINE_STRIP else GL11.GL_LINES
    }

    val stairOuterF = object : Geometry() {
        override val name: String = "stairOuterF"
        override fun render(pt: Double, params: List<Double>) {
            val (_x, _y, _z, _c) = params
            val (x, y, z, s) = rescale(_x, _y , _z)
            val verts = stairOuterVertices[_c.toInt()]

            begin(GL11.GL_TRIANGLE_STRIP, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
            if (Renderer.USE_NEW_SHIT) {
                verts.forEachIndexed { i, v -> if (i > 0) addVert(x + v.x * s, y + v.y * s, z + v.z * s) }

                index(13)
                index(11)
                index(3)
                index(7)
                index(1)
                index(5)
                index(0)
                index(4)
                index(2)
                index(8)
                index(3)
                index(9)
                index(13)
                index(12)
                index(11)
                index(10)
                index(7)
                index(6)
                index(5)
                index(4)

                reset()
                index(10)
                index(12)
                index(6)
                index(9)
                reset()
                index(9)
                index(8)
                index(6)
                index(4)

                reset()
                index(1)
                index(0)
                index(3)
                index(2)
            } else {
                fun emit(i: Int) = pos(x + verts[i + 1].x * s, y + verts[i + 1].y * s, z + verts[i + 1].z * s)

                emit(13)
                emit(11)
                emit(3)
                emit(7)
                emit(1)
                emit(5)
                emit(0)
                emit(4)
                emit(2)
                emit(8)
                emit(3)
                emit(9)
                emit(13)
                emit(12)
                emit(11)
                emit(10)
                emit(7)
                emit(6)
                emit(5)
                emit(4)
                emit(4)
                emit(6)
                emit(8)
                emit(9)
                emit(9)
                emit(6)
                emit(12)
                emit(10)

                emit(10)
                emit(1)
                emit(1)
                emit(0)
                emit(3)
                emit(2)
            }
            draw()
        }

        override fun testPoints(params: List<Double>): Array<Point> {
            val (x, y, z) = params
            return arrayOf(
                Point(x + 0.5, y + 0.5, z + 0.5),
                Point(x + 0.5, y + 0.5, z - 0.5),
                Point(x + 0.5, y - 0.5, z + 0.5),
                Point(x + 0.5, y - 0.5, z - 0.5),
                Point(x - 0.5, y + 0.5, z + 0.5),
                Point(x - 0.5, y + 0.5, z - 0.5),
                Point(x - 0.5, y - 0.5, z + 0.5),
                Point(x - 0.5, y - 0.5, z - 0.5)
            )
        }

        override fun getVertexCount(params: List<Double>): Int = 14
        override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 35 else 34
        override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
    }
}