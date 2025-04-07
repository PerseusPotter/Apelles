package com.perseuspotter.apelles.geo

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.depression.VAO
import com.perseuspotter.apelles.state.GlState
import com.perseuspotter.apelles.state.Thingamabob
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL31
import java.nio.IntBuffer
import kotlin.math.pow
import kotlin.math.sqrt

abstract class Geometry {
    companion object {
        @JvmField
        val tess = Tessellator.getInstance()
        @JvmField
        val worldRen = tess.worldRenderer
        private val rxf = RenderManager::class.java.getDeclaredField("field_78725_b")
        private val ryf = RenderManager::class.java.getDeclaredField("field_78726_c")
        private val rzf = RenderManager::class.java.getDeclaredField("field_78723_d")
        init {
            rxf.isAccessible = true
            ryf.isAccessible = true
            rzf.isAccessible = true
        }
        @JvmField
        val rm = Minecraft.getMinecraft().renderManager
        @JvmStatic
        fun getRenderX(): Double = rxf.getDouble(rm)
        @JvmStatic
        fun getRenderY(): Double = ryf.getDouble(rm)
        @JvmStatic
        fun getRenderZ(): Double = rzf.getDouble(rm)

        @JvmStatic
        fun begin(mode: Int, tex: Boolean, x: Double, y: Double, z: Double) {
            cx = x
            cy = y
            cz = z
            if (Renderer.USE_NEW_SHIT) return
            if (GlState.isLightingEnabled()) {
                if (tex) worldRen.begin(mode, DefaultVertexFormats.POSITION_TEX_NORMAL)
                else worldRen.begin(mode, DefaultVertexFormats.POSITION_NORMAL)
            } else {
                if (tex) worldRen.begin(mode, DefaultVertexFormats.POSITION_TEX)
                else worldRen.begin(mode, DefaultVertexFormats.POSITION)
            }
        }
        @JvmStatic
        fun draw() {
            if (!Renderer.USE_NEW_SHIT) tess.draw()
        }
        protected var cx = 0.0
        protected var cy = 0.0
        protected var cz = 0.0
        @JvmStatic
        fun pos(x: Double, y: Double, z: Double) {
            if (Renderer.USE_NEW_SHIT) {
                addVert(x, y, z)
                index(currBuf!!.vertCount - vertOffset - 1)
                return
            }
            if (GlState.isLightingEnabled()) pos(x, y, z, x - cx, y - cy, z - cz)
            else worldRen.pos(x, y, z).endVertex()
        }
        @JvmStatic
        fun pos(x: Double, y: Double, z: Double, u: Double, v: Double) {
            if (Renderer.USE_NEW_SHIT) {
                addVert(x, y, z, u, v)
                index(currBuf!!.vertCount - vertOffset - 1)
                return
            }
            if (GlState.isLightingEnabled()) pos(x, y, z, u, v, x - cx, y - cy, z - cz)
            else worldRen.pos(x, y, z).tex(u, v).endVertex()
        }
        @JvmStatic
        fun pos(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double) {
            if (Renderer.USE_NEW_SHIT) {
                addVert(x, y, z, nx, ny, nz)
                index(currBuf!!.vertCount - vertOffset - 1)
                return
            }
            if (GlState.isLightingEnabled()) {
                val l = 1.0 / sqrt(nx * nx + ny * ny + nz * nz)
                worldRen.pos(x, y, z).normal((nx * l).toFloat(), (ny * l).toFloat(), (nz * l).toFloat()).endVertex()
            } else worldRen.pos(x, y, z).endVertex()
        }
        @JvmStatic
        fun pos(x: Double, y: Double, z: Double, u: Double, v: Double, nx: Double, ny: Double, nz: Double) {
            if (Renderer.USE_NEW_SHIT) {
                addVert(x, y, z, u, v, nx, ny, nz)
                index(currBuf!!.vertCount - vertOffset - 1)
                return
            }
            if (GlState.isLightingEnabled()) {
                val l = 1.0 / sqrt(nx * nx + ny * ny + nz * nz)
                worldRen.pos(x, y, z).tex(u, v).normal((nx * l).toFloat(), (ny * l).toFloat(), (nz * l).toFloat()).endVertex()
            } else worldRen.pos(x, y, z).tex(u, v).endVertex()
        }

        private val SQRT_2 = sqrt(2.0)
        @JvmStatic
        fun rescale(x: Double, y: Double, z: Double): DoubleArray {
            val rx = getRenderX()
            val ry = getRenderY() + (Minecraft.getMinecraft().thePlayer?.getEyeHeight() ?: 0f) - 0.1
            val rz = getRenderZ()
            val d = (rx - x).pow(2) + (ry - y).pow(2) + (rz - z).pow(2)
            val rd = (Minecraft.getMinecraft().gameSettings.renderDistanceChunks shl 4) * SQRT_2
            if (d >= rd * rd) {
                val f = rd / sqrt(d)
                return doubleArrayOf(
                    rx + (x - rx) * f,
                    ry + (y - ry) * f,
                    rz + (z - rz) * f,
                    f
                )
            }
            return doubleArrayOf(x, y, z, 1.0)
        }

        @JvmStatic
        fun toTriangleStrip(N: Int, triangles: Array<IntArray>, primitiveRestartIndex: Boolean): IntBuffer {
            val vert2 = mutableMapOf<Int, IntArray>()
            val vert1 = Array<MutableSet<Int>>(N) { mutableSetOf() }
            fun get(a: IntArray?) = if (a == null) -1 else if (a[1] == -1) a[0] else a[1]
            fun add(a: IntArray, v: Int) {
                if (a[0] == -1) a[0] = v
                else a[1] = v
            }
            fun del(a: IntArray, v: Int) {
                if (a[1] == v) a[1] = -1
                else {
                    a[0] = a[1]
                    a[1] = -1
                }
            }
            triangles.forEachIndexed { i, tri ->
                val (v1, v2, v3) = tri

                add(vert2.getOrPut(v1 + v2 * N - 1) { IntArray(2) { -1 } }, i)
                add(vert2.getOrPut(v2 + v3 * N - 1) { IntArray(2) { -1 } }, i)
                add(vert2.getOrPut(v3 + v1 * N - 1) { IntArray(2) { -1 } }, i)

                vert1[v1].add(i)
                vert1[v2].add(i)
                vert1[v3].add(i)
            }

            val strip = IntBuffer.allocate(3 + 5 * (triangles.size - 1))
            var pv = -1
            var ppv = -1
            var ccw = true
            fun emit(v: Int) {
                strip.put(v)
                ppv = pv
                pv = v
                ccw = !ccw
            }
            val remaining = MutableList(triangles.size) { it }.toMutableSet()
            fun remove(i: Int) {
                val (v1, v2, v3) = triangles[i]

                vert2[v1 + v2 * N - 1]?.let { del(it, i) }
                vert2[v2 + v3 * N - 1]?.let { del(it, i) }
                vert2[v3 + v1 * N - 1]?.let { del(it, i) }

                vert1[v1].remove(i)
                vert1[v2].remove(i)
                vert1[v3].remove(i)

                remaining.remove(i)
            }

            val iter = remaining.iterator()
            val tri = iter.next()
            iter.remove()
            emit(triangles[tri][0])
            emit(triangles[tri][1])
            emit(triangles[tri][2])
            remove(tri)

            while (remaining.size > 0) {
                val v1 = if (ccw) ppv else pv
                val v2 = if (ccw) pv else ppv
                val t2 = get(vert2[v1 + v2 * N - 1])
                if (t2 >= 0) {
                    val (tv1, tv2, tv3) = triangles[t2]
                    emit(if (tv1 == v1) tv3 else if (tv2 == v1) tv1 else tv2)
                    remove(t2)
                    continue
                }

                val nv1 = if (vert1[pv].size > 0) pv else if (vert1[ppv].size > 0) ppv else -1
                if (nv1 >= 0) {
                    val iter = vert1[nv1].iterator()
                    val t1 = iter.next()
                    iter.remove()
                    val tri = triangles[t1]
                    val i = if (tri[0] == nv1) 0 else if (tri[1] == nv1) 1 else 2

                    if (primitiveRestartIndex) {
                        emit(-1)
                        // ppv = -1
                        // pv = -1
                        ccw = true
                    } else if (nv1 == ppv) {
                        emit(nv1)
                    }

                    emit(nv1)
                    if (ccw) {
                        emit(tri[if (i == 0) 2 else i - 1])
                        emit(tri[if (i <= 1) i + 1 else 0])
                    } else {
                        emit(tri[if (i == 2) 0 else i + 1])
                        emit(tri[if (i >= 1) i - 1 else 2])
                    }

                    remove(t1)
                    continue
                }

                val iter = remaining.iterator()
                val tri = iter.next()
                iter.remove()
                val (tv1, tv2, tv3) = triangles[tri]

                if (primitiveRestartIndex) {
                    emit(-1)
                    // ppv = -1
                    // pv = -1
                    ccw = true
                } else {
                    emit(pv)
                    emit(tv1)
                }

                emit(tv1)
                if (ccw) {
                    emit(tv3)
                    emit(tv2)
                } else {
                    emit(tv2)
                    emit(tv3)
                }

                remove(tri)
            }

            strip.flip()
            return strip
        }

        val buffers = Array(4) { mutableMapOf<Int, VAO>() }
        var currBufM = buffers[0]
        var currBuf: VAO? = null
        var currBufI: VAOInfo? = null
        val unusedBufs = mutableSetOf<Int>()
        val bufInfo = mutableMapOf<Int, VAOInfo>()
        var PRIMITIVE_RESTART_INDEX = 0xFFFF
        data class VAOInfo(var vert: Int, var index: Int, val c: Boolean, val n: Boolean, val t: Boolean, val m: Int, val th: Thingamabob) {
            fun add(v: Int, i: Int) = apply {
                vert += v
                index += i
            }
        }

        fun bindBufGroup(i: Int) {
            currBufM = buffers[i]
            unusedBufs.clear()
            unusedBufs.addAll(currBufM.keys)
            bufInfo.clear()
        }
        fun allocate(k: Int, v: Int, i: Int, c: Boolean, n: Boolean, t: Boolean, m: Int, th: Thingamabob) {
            bufInfo.getOrPut(k) { VAOInfo(0, 0, c, n, t, m, th) }.add(v, i)
        }
        fun prepare() {
            bufInfo.forEach{ (k, s) ->
                if (s.vert == 0 || s.index == 0) return
                unusedBufs.remove(k)
                if (!currBufM.containsKey(k)) currBufM[k] = VAO(s.vert, s.index, s.c, s.n, s.t, s.m)
                else if (currBufM[k]!!.MAX_VERTEX_COUNT < s.vert || currBufM[k]!!.MAX_INDEX_COUNT < s.index) {
                    currBufM[k]!!.destroy()
                    currBufM[k] = VAO(s.vert, s.index, s.c, s.n, s.t, s.m)
                } else currBufM[k]!!.reset()
            }
            unusedBufs.forEach { currBufM.remove(it)!!.destroy() }
        }
        fun render() {
            currBufM.forEach { (k, v) ->
                val i = bufInfo[k]!!
                PRIMITIVE_RESTART_INDEX = i.vert
                GL31.glPrimitiveRestartIndex(PRIMITIVE_RESTART_INDEX)
                i.th.prerender()
                GlState.setColorArray(i.c)
                GlState.setNormalArray(i.n)
                GlState.setTexArray(i.t)
                v.update()
                v.draw()
            }
            GL30.glBindVertexArray(0)
        }

        fun bind(k: Int) {
            currBuf = currBufM[k]
            currBufI = bufInfo[k]
            vertOffset = currBuf!!.vertCount
            PRIMITIVE_RESTART_INDEX = bufInfo[k]!!.vert
            reset()
        }

        private fun addNormVert(nx: Double, ny: Double, nz: Double) {
            if (!currBufI!!.n) return
            val l = 1.0 / sqrt(nx * nx + ny * ny + nz * nz)
            currBuf!!.putN((nx * l).toFloat(), (ny * l).toFloat(), (nz * l).toFloat())
        }

        @JvmStatic
        fun addVert(x: Double, y: Double, z: Double) {
            addVert(x, y, z, x - cx, y - cy, z - cz)
        }
        @JvmStatic
        fun addVert(x: Double, y: Double, z: Double, u: Double, v: Double) {
            addVert(x, y, z, u, v, x - cx, y - cy, z - cz)
        }
        @JvmStatic
        fun addVert(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double) {
            currBuf!!.putP(x.toFloat(), y.toFloat(), z.toFloat())
            addNormVert(nx, ny, nz)
        }
        @JvmStatic
        fun addVert(x: Double, y: Double, z: Double, u: Double, v: Double, nx: Double, ny: Double, nz: Double) {
            currBuf!!.putP(x.toFloat(), y.toFloat(), z.toFloat())
            addNormVert(nx, ny, nz)
            currBuf!!.putT(u.toFloat(), v.toFloat())
        }
        var vertOffset = 0
        @JvmStatic
        fun index(i: Int) {
            currBuf!!.putI(if (i == PRIMITIVE_RESTART_INDEX) i else i + vertOffset)
        }
        @JvmStatic
        fun reset() {
            currBuf!!.putI(PRIMITIVE_RESTART_INDEX)
        }
    }
    abstract val name: String
    abstract fun render(pt: Double, params: DoubleArray)
    abstract fun testPoints(params: DoubleArray): Array<Point>
    abstract fun getVertexCount(params: DoubleArray): Int
    abstract fun getIndicesCount(params: DoubleArray): Int
    abstract fun getDrawMode(): Int
}
operator fun DoubleArray.component6() = get(5)
operator fun DoubleArray.component7() = get(6)