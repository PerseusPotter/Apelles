package com.perseuspotter.apelles.geo

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.depression.PerFrameCache
import com.perseuspotter.apelles.depression.VAO
import com.perseuspotter.apelles.state.Color
import com.perseuspotter.apelles.state.GlState
import com.perseuspotter.apelles.state.Thingamabob
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.*

abstract class Geometry {
    companion object {
        @JvmField
        val tess = Tessellator.getInstance()
        @JvmField
        val worldRen = tess.worldRenderer
        @JvmField
        val rm = Minecraft.getMinecraft().renderManager
        private val rxf = RenderManager::class.java.getDeclaredField("field_78725_b").also { it.isAccessible = true }
        private val ryf = RenderManager::class.java.getDeclaredField("field_78726_c").also { it.isAccessible = true }
        private val rzf = RenderManager::class.java.getDeclaredField("field_78723_d").also { it.isAccessible = true }
        @JvmStatic
        protected var rxc = PerFrameCache(0.0) { rxf.getDouble(rm) }
        @JvmStatic
        protected var ryc = PerFrameCache(0.0) { ryf.getDouble(rm) }
        @JvmStatic
        protected var rzc = PerFrameCache(0.0) { rzf.getDouble(rm) }
        @JvmStatic
        fun getRenderX(): Double = rxc.get()
        @JvmStatic
        fun getRenderY(): Double = ryc.get()
        @JvmStatic
        fun getRenderZ(): Double = rzc.get()

        private val SQRT_2 = sqrt(2.0)
        private var fpdc = PerFrameCache(0.0) { (Minecraft.getMinecraft().gameSettings.renderDistanceChunks shl 4) * SQRT_2 }
        @JvmStatic
        fun getFarPlaneDist() = fpdc.get()

        @JvmField
        val viewProjMatrix = BufferUtils.createFloatBuffer(16)
        @JvmField
        val MODELVIEW: FloatBuffer = ActiveRenderInfo::class.java.getDeclaredField("field_178812_b").also { it.isAccessible = true }.get(null) as FloatBuffer
        @JvmField
        val PROJECTION: FloatBuffer = ActiveRenderInfo::class.java.getDeclaredField("field_178813_c").also { it.isAccessible = true }.get(null) as FloatBuffer

        private val dummyCameraCache = PerFrameCache(null) { updateCameraInfo() }
        private var cameraFV = Point(0.0, 0.0, 0.0)
        private var cameraUV = Point(0.0, 0.0, 0.0)
        private var cameraRV = Point(0.0, 0.0, 0.0)
        @JvmStatic
        fun getCameraFV() = dummyCameraCache.get().let { cameraFV }
        @JvmStatic
        fun getCameraUV() = dummyCameraCache.get().let { cameraUV }
        @JvmStatic
        fun getCameraRV() = dummyCameraCache.get().let { cameraRV }

        private fun updateCameraInfo() {
            val view = FloatArray(16)
            val proj = FloatArray(16)
            MODELVIEW.get(view).rewind()
            PROJECTION.get(proj).rewind()

            viewProjMatrix.clear()
            viewProjMatrix.put(proj[0] * view[0] + proj[4] * view[1] + proj[8] * view[2] + proj[12] * view[3])
            viewProjMatrix.put(proj[1] * view[0] + proj[5] * view[1] + proj[9] * view[2] + proj[13] * view[3])
            viewProjMatrix.put(proj[2] * view[0] + proj[6] * view[1] + proj[10] * view[2] + proj[14] * view[3])
            viewProjMatrix.put(proj[3] * view[0] + proj[7] * view[1] + proj[11] * view[2] + proj[15] * view[3])

            viewProjMatrix.put(proj[0] * view[4] + proj[4] * view[5] + proj[8] * view[6] + proj[12] * view[7])
            viewProjMatrix.put(proj[1] * view[4] + proj[5] * view[5] + proj[9] * view[6] + proj[13] * view[7])
            viewProjMatrix.put(proj[2] * view[4] + proj[6] * view[5] + proj[10] * view[6] + proj[14] * view[7])
            viewProjMatrix.put(proj[3] * view[4] + proj[7] * view[5] + proj[11] * view[6] + proj[15] * view[7])

            viewProjMatrix.put(proj[0] * view[8] + proj[4] * view[9] + proj[8] * view[10] + proj[12] * view[11])
            viewProjMatrix.put(proj[1] * view[8] + proj[5] * view[9] + proj[9] * view[10] + proj[13] * view[11])
            viewProjMatrix.put(proj[2] * view[8] + proj[6] * view[9] + proj[10] * view[10] + proj[14] * view[11])
            viewProjMatrix.put(proj[3] * view[8] + proj[7] * view[9] + proj[11] * view[10] + proj[15] * view[11])

            viewProjMatrix.put(proj[0] * view[12] + proj[4] * view[13] + proj[8] * view[14] + proj[12] * view[15])
            viewProjMatrix.put(proj[1] * view[12] + proj[5] * view[13] + proj[9] * view[14] + proj[13] * view[15])
            viewProjMatrix.put(proj[2] * view[12] + proj[6] * view[13] + proj[10] * view[14] + proj[14] * view[15])
            viewProjMatrix.put(proj[3] * view[12] + proj[7] * view[13] + proj[11] * view[14] + proj[15] * view[15])
            viewProjMatrix.flip()

            val yaw = rm.playerViewY / 180.0 * PI
            val pit = rm.playerViewX / 180.0 * PI
            cameraFV = Point(
                -sin(yaw) * cos(pit),
                -sin(pit),
                cos(yaw) * cos(pit)
            )
            cameraUV = Point(
                -sin(yaw) * sin(pit),
                cos(pit),
                cos(yaw) * sin(pit)
            )
            cameraRV = Point(
                cameraFV.y * cameraUV.z - cameraFV.z * cameraUV.y,
                cameraFV.z * cameraUV.x - cameraFV.x * cameraUV.z,
                cameraFV.x * cameraUV.y - cameraFV.y * cameraUV.x
            )
        }

        @JvmStatic
        fun rescale(x: Double, y: Double, z: Double): DoubleArray {
            val dy = (Minecraft.getMinecraft().thePlayer?.getEyeHeight() ?: 0f) - 0.1
            val rx = getRenderX()
            val ry = getRenderY()
            val rz = getRenderZ()
            val d = (rx - x).pow(2) + (ry + dy - y).pow(2) + (rz - z).pow(2)
            val fd = getFarPlaneDist()
            if (d >= fd * fd) {
                val f = fd / sqrt(d)
                return doubleArrayOf(
                    rx + (x - rx) * f,
                    ry + dy + (y - ry - dy) * f,
                    rz + (z - rz) * f,
                    f
                )
            }
            return doubleArrayOf(x, y, z, 1.0)
        }

        @JvmStatic
        fun increase(x: Double, y: Double, z: Double): Double {
            val dy = (Minecraft.getMinecraft().thePlayer?.getEyeHeight() ?: 0f) - 0.1
            val rx = getRenderX()
            val ry = getRenderY()
            val rz = getRenderZ()
            val d = sqrt((rx - x).pow(2) + (ry + dy - y).pow(2) + (rz - z).pow(2))
            return min(10.0, d * 0.05 + 1.0)
        }

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
            else worldRen.pos(x - getRenderX(), y - getRenderY(), z - getRenderZ()).endVertex()
        }
        @JvmStatic
        fun pos(x: Double, y: Double, z: Double, u: Double, v: Double) {
            if (Renderer.USE_NEW_SHIT) {
                addVert(x, y, z, u, v)
                index(currBuf!!.vertCount - vertOffset - 1)
                return
            }
            if (GlState.isLightingEnabled()) pos(x, y, z, u, v, x - cx, y - cy, z - cz)
            else worldRen.pos(x - getRenderX(), y - getRenderY(), z - getRenderZ()).tex(u, v).endVertex()
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
                worldRen.pos(x - getRenderX(), y - getRenderY(), z - getRenderZ()).normal((nx * l).toFloat(), (ny * l).toFloat(), (nz * l).toFloat()).endVertex()
            } else worldRen.pos(x - getRenderX(), y - getRenderY(), z - getRenderZ()).endVertex()
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
                worldRen.pos(x - getRenderX(), y - getRenderY(), z - getRenderZ()).tex(u, v).normal((nx * l).toFloat(), (ny * l).toFloat(), (nz * l).toFloat()).endVertex()
            } else worldRen.pos(x - getRenderX(), y - getRenderY(), z - getRenderZ()).tex(u, v).endVertex()
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
        var currCol: Color? = null
        var prevK: Int? = null
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
            currBufI = null
            prevK = null
        }
        fun allocate(k: Int, v: Int, i: Int, c: Boolean, n: Boolean, t: Boolean, m: Int, th: Thingamabob) {
            (
                if (k == prevK) currBufI!!
                else bufInfo.getOrPut(k) { VAOInfo(0, 0, c, n, t, m, th) }.also {
                    currBufI = it
                    prevK = k
                }
            ).add(v, i)
        }
        fun prepare() {
            currBufI = null
            prevK = null
            bufInfo.forEach{ (k, s) ->
                unusedBufs.remove(k)
                if (!currBufM.containsKey(k)) currBufM[k] = VAO(s.vert, s.index, s.c, s.n, s.t, s.m)
                else if (currBufM[k]!!.MAX_VERTEX_COUNT < s.vert || currBufM[k]!!.MAX_INDEX_COUNT < s.index) {
                    currBufM[k]!!.destroy()
                    currBufM[k] = VAO(s.vert, s.index, s.c, s.n, s.t, s.m)
                } else currBufM[k]!!.reset()
                if (s.vert > PRIMITIVE_RESTART_INDEX) PRIMITIVE_RESTART_INDEX = s.vert
            }
            unusedBufs.forEach { currBufM.remove(it)!!.destroy() }
        }
        fun render(pt: Double) {
            GlState.setPrimitiveRestart(PRIMITIVE_RESTART_INDEX)
            currBufM.forEach { (k, v) ->
                val i = bufInfo[k]!!
                i.th.prerender(pt)
                GlState.setColorArray(i.c)
                GlState.setNormalArray(i.n)
                GlState.setTexArray(i.t)
                v.update()
                v.draw()
            }
            GL30.glBindVertexArray(0)
        }

        fun bind(thing: Thingamabob) {
            val k = thing.getVBOGroupingId()
            if (k != prevK) {
                prevK = k
                currBuf = currBufM[k]
                currBufI = bufInfo[k]
            }
            currCol = thing.color
            vertOffset = currBuf!!.vertCount
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
            currBuf!!.putP((x - getRenderX()).toFloat(), (y - getRenderY()).toFloat(), (z - getRenderZ()).toFloat())
            currBuf!!.putC(currCol!!.r, currCol!!.g, currCol!!.b, currCol!!.a)
            addNormVert(nx, ny, nz)
        }
        @JvmStatic
        fun addVert(x: Double, y: Double, z: Double, u: Double, v: Double, nx: Double, ny: Double, nz: Double) {
            currBuf!!.putP((x - getRenderX()).toFloat(), (y - getRenderY()).toFloat(), (z - getRenderZ()).toFloat())
            currBuf!!.putC(currCol!!.r, currCol!!.g, currCol!!.b, currCol!!.a)
            addNormVert(nx, ny, nz)
            currBuf!!.putT(u.toFloat(), v.toFloat())
        }
        @JvmStatic
        fun addVertRaw(x: Double, y: Double, z: Double) {
            currBuf!!.putP(x.toFloat(), y.toFloat(), z.toFloat())
            currBuf!!.putC(currCol!!.r, currCol!!.g, currCol!!.b, currCol!!.a)
            addNormVert(0.0, 0.0, 0.0)
        }
        @JvmStatic
        fun addVertRaw(x: Double, y: Double, z: Double, u: Double, v: Double) {
            currBuf!!.putP(x.toFloat(), y.toFloat(), z.toFloat())
            currBuf!!.putC(currCol!!.r, currCol!!.g, currCol!!.b, currCol!!.a)
            addNormVert(0.0, 0.0, 0.0)
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
    abstract fun render(pt: Double, params: List<Double>)
    abstract fun inView(params: List<Double>): Boolean
    abstract fun getVertexCount(params: List<Double>): Int
    abstract fun getIndicesCount(params: List<Double>): Int
    abstract fun getDrawMode(params: List<Double>): Int
}
operator fun <E> List<E>.component6() = get(5)
operator fun <E> List<E>.component7() = get(6)
operator fun <E> List<E>.component8() = get(7)
operator fun <E> List<E>.component9() = get(8)
operator fun <E> List<E>.component10() = get(9)
operator fun <E> List<E>.component11() = get(10)
operator fun <E> List<E>.component12() = get(11)
operator fun <E> List<E>.component13() = get(12)