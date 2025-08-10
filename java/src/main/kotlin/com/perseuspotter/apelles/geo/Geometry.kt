package com.perseuspotter.apelles.geo

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.depression.PerFrameCache
import com.perseuspotter.apelles.depression.VAO
import com.perseuspotter.apelles.state.Color
import com.perseuspotter.apelles.state.Thingamabob
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormat
import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.Vector3f
import java.nio.FloatBuffer
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

        private val dummyCameraCache = PerFrameCache(null) { updateCameraInfo() }
        private val MODELVIEW: FloatBuffer = ActiveRenderInfo::class.java.getDeclaredField("field_178812_b").also { it.isAccessible = true }.get(null) as FloatBuffer
        private val PROJECTION: FloatBuffer = ActiveRenderInfo::class.java.getDeclaredField("field_178813_c").also { it.isAccessible = true }.get(null) as FloatBuffer
        private val mvpMatrix = BufferUtils.createFloatBuffer(16)
        private val normalMatrix = BufferUtils.createFloatBuffer(9)
        private data class DirectionalLight(val direction: Vector3f, val ambient: Vector3f, val diffuse: Vector3f, val specular: Vector3f)
        private val lightsStatic = arrayOf(
            DirectionalLight(
                Vector3f(0f, -1f, 0f).let { it.normalise(it) },
                Vector3f(0f, 0f, 0f),
                Vector3f(0.6f, 0.6f, 0.6f),
                Vector3f(0.3f, 0.3f, 0.3f)
            ),
            DirectionalLight(
                Vector3f(0f, 0.3f, -1.0f).let { it.normalise(it) },
                Vector3f(0f, 0f, 0f),
                Vector3f(0.3f, 0.3f, 0.3f),
                Vector3f(0f, 0f, 0f)
            ),
            DirectionalLight(
                Vector3f(0f, 0.3f, 1.0f).let { it.normalise(it) },
                Vector3f(0f, 0f, 0f),
                Vector3f(0.3f, 0.3f, 0.3f),
                Vector3f(0f, 0f, 0f)
            ),
            DirectionalLight(
                Vector3f(-1.0f, 0.3f, 0f).let { it.normalise(it) },
                Vector3f(0f, 0f, 0f),
                Vector3f(0.3f, 0.3f, 0.3f),
                Vector3f(0f, 0f, 0f)
            ),
            DirectionalLight(
                Vector3f(1.0f, 0.3f, 0f).let { it.normalise(it) },
                Vector3f(0f, 0f, 0f),
                Vector3f(0.3f, 0.3f, 0.3f),
                Vector3f(0f, 0f, 0f)
            )
        )
        private val lights = BufferUtils.createFloatBuffer(lightsStatic.size * 4 * 4)
        @JvmStatic
        fun getModelViewMatrix() = dummyCameraCache.get().let { MODELVIEW }
        @JvmStatic
        fun getProjectionMatrix() = dummyCameraCache.get().let { PROJECTION }
        @JvmStatic
        fun getMVPMatrix(): FloatBuffer = dummyCameraCache.get().let { mvpMatrix }
        @JvmStatic
        fun getNormalMatrix(): FloatBuffer = dummyCameraCache.get().let { normalMatrix }
        @JvmStatic
        fun getLights(): FloatBuffer = dummyCameraCache.get().let { lights }

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

            mvpMatrix.clear()
            mvpMatrix.put(proj[0] * view[0] + proj[4] * view[1] + proj[8] * view[2] + proj[12] * view[3])
            mvpMatrix.put(proj[1] * view[0] + proj[5] * view[1] + proj[9] * view[2] + proj[13] * view[3])
            mvpMatrix.put(proj[2] * view[0] + proj[6] * view[1] + proj[10] * view[2] + proj[14] * view[3])
            mvpMatrix.put(proj[3] * view[0] + proj[7] * view[1] + proj[11] * view[2] + proj[15] * view[3])

            mvpMatrix.put(proj[0] * view[4] + proj[4] * view[5] + proj[8] * view[6] + proj[12] * view[7])
            mvpMatrix.put(proj[1] * view[4] + proj[5] * view[5] + proj[9] * view[6] + proj[13] * view[7])
            mvpMatrix.put(proj[2] * view[4] + proj[6] * view[5] + proj[10] * view[6] + proj[14] * view[7])
            mvpMatrix.put(proj[3] * view[4] + proj[7] * view[5] + proj[11] * view[6] + proj[15] * view[7])

            mvpMatrix.put(proj[0] * view[8] + proj[4] * view[9] + proj[8] * view[10] + proj[12] * view[11])
            mvpMatrix.put(proj[1] * view[8] + proj[5] * view[9] + proj[9] * view[10] + proj[13] * view[11])
            mvpMatrix.put(proj[2] * view[8] + proj[6] * view[9] + proj[10] * view[10] + proj[14] * view[11])
            mvpMatrix.put(proj[3] * view[8] + proj[7] * view[9] + proj[11] * view[10] + proj[15] * view[11])

            mvpMatrix.put(proj[0] * view[12] + proj[4] * view[13] + proj[8] * view[14] + proj[12] * view[15])
            mvpMatrix.put(proj[1] * view[12] + proj[5] * view[13] + proj[9] * view[14] + proj[13] * view[15])
            mvpMatrix.put(proj[2] * view[12] + proj[6] * view[13] + proj[10] * view[14] + proj[14] * view[15])
            mvpMatrix.put(proj[3] * view[12] + proj[7] * view[13] + proj[11] * view[14] + proj[15] * view[15])
            mvpMatrix.flip()

            val normalMatArr = view.let {
                val det = max(
                    1e-6f,
                    it[0] * (it[5] * it[10] - it[9] * it[6]) -
                    it[4] * (it[1] * it[10] - it[9] * it[2]) +
                    it[8] * (it[1] * it[6] - it[5] * it[2])
                )
                val f = 1f / det
                floatArrayOf(
                    f * (it[5] * it[10] - it[9] * it[6]), f * (it[8] * it[6] - it[4] * it[10]), f * (it[4] * it[9] - it[8] * it[5]),
                    f * (it[9] * it[2] - it[1] * it[10]), f * (it[0] * it[10] - it[8] * it[2]), f * (it[8] * it[1] - it[0] * it[9]),
                    f * (it[1] * it[6] - it[5] * it[2]), f * (it[4] * it[2] - it[0] * it[6]), f * (it[0] * it[5] - it[4] * it[1])
                )
            }
            normalMatrix.clear()
            normalMatrix.put(normalMatArr)
            normalMatrix.flip()

            lights.clear()
            lightsStatic.forEach {
                val x = normalMatArr[0] * it.direction.x + normalMatArr[3] * it.direction.y + normalMatArr[6] * it.direction.z
                val y = normalMatArr[1] * it.direction.x + normalMatArr[4] * it.direction.y + normalMatArr[7] * it.direction.z
                val z = normalMatArr[2] * it.direction.x + normalMatArr[5] * it.direction.y + normalMatArr[8] * it.direction.z
                val f = 1f / sqrt(x * x + y * y + z * z)
                lights.put(x * f)
                lights.put(y * f)
                lights.put(z * f)
                lights.put(0f)
                lights.put(it.ambient.x)
                lights.put(it.ambient.y)
                lights.put(it.ambient.z)
                lights.put(0f)
                lights.put(it.diffuse.x)
                lights.put(it.diffuse.y)
                lights.put(it.diffuse.z)
                lights.put(0f)
                lights.put(it.specular.x)
                lights.put(it.specular.y)
                lights.put(it.specular.z)
                lights.put(0f)
            }
            lights.flip()

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

        val buffers = Array(4) { mutableMapOf<Int, VAO>() }
        var currBufM = buffers[0]
        var currBuf: VAO? = null
        var currBufI: VAOInfo? = null
        var currCol: Color? = null
        var prevK: Int? = null
        val unusedBufs = mutableSetOf<Int>()
        val bufInfo = mutableMapOf<Int, VAOInfo>()
        var lightType = 0
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
            }
            unusedBufs.forEach { currBufM.remove(it)!!.destroy() }
        }
        fun render(pt: Double) {
            currBufM.forEach { (k, v) ->
                val i = bufInfo[k]!!
                i.th.prerender(pt)
                v.updateAndDraw()
            }
        }

        fun bind(thing: Thingamabob, c: Boolean, t: Boolean) {
            val k = thing.getVBOGroupingId()
            if (k != prevK) {
                prevK = k
                if (Renderer.USE_NEW_SHIT) currBuf = currBufM[k]
                currBufI = bufInfo[k] ?: VAOInfo(0, 0, c, thing.lighting > 0, t, thing.getDrawMode(), thing)
            }
            currCol = thing.color
            if (Renderer.USE_NEW_SHIT) vertOffset = currBuf!!.vertCount
            lightType = thing.lighting
        }

        var vertOffset = 0
        fun index(i: Int) {
            currBuf!!.putI(i + vertOffset)
        }

        val vertexFormats = arrayOf(
            DefaultVertexFormats.POSITION,
            DefaultVertexFormats.POSITION_TEX,
            DefaultVertexFormats.POSITION_COLOR,
            DefaultVertexFormats.POSITION_TEX_COLOR,
            DefaultVertexFormats.POSITION_NORMAL,
            DefaultVertexFormats.POSITION_TEX_NORMAL,
            VertexFormat().addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B),
            DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL
        )
    }
    abstract val name: String
    var currentParams: List<Double> = listOf()
    abstract fun render(pt: Double)
    abstract fun inView(): Boolean
    abstract fun getVertexCount(): Int
    abstract fun getIndexCount(): Int
    abstract fun getDrawMode(): Int

    private var isTex = false
    private var hasColor = false
    private var emitColor = false
    private var vboIdx = 0
    private var pvbo = doubleArrayOf()
    private var cvbo = floatArrayOf()
    private var nvbo = doubleArrayOf()
    private var tvbo = doubleArrayOf()
    private var iboC = intArrayOf()
    private var iboI = 0
    private var ibo = intArrayOf()
    private var emitC = 0

    fun begin(mode: Int, tex: Boolean, color: Boolean = false) {
        isTex = tex && currBufI!!.t
        emitColor = (color || Renderer.USE_NEW_SHIT) && currBufI!!.c
        hasColor = color && currBufI!!.c

        vboIdx = 0
        val vertCount = getVertexCount()
        pvbo = DoubleArray(vertCount * 3)
        if (hasColor) cvbo = FloatArray(vertCount * 4)
        if (lightType == 1) {
            nvbo = DoubleArray(vertCount * 3)
            iboC = IntArray(vertCount)
            if (!Renderer.USE_NEW_SHIT) {
                iboI = 0
                ibo = IntArray(getIndexCount())
            }
        }
        if (isTex) tvbo = DoubleArray(vertCount * 2)
        emitC = 0

        if (!Renderer.USE_NEW_SHIT) worldRen.begin(
            mode,
            vertexFormats[
                (if (isTex) 1 else 0) or
                (if (emitColor) 2 else 0) or
                (if (lightType > 0) 4 else 0)
            ]
        )
    }

    fun draw() {
        if (lightType == 1) {
            for (i in iboC.indices) {
                nvbo[i * 3 + 0] = nvbo[i * 3 + 0] / iboC[i]
                nvbo[i * 3 + 1] = nvbo[i * 3 + 1] / iboC[i]
                nvbo[i * 3 + 2] = nvbo[i * 3 + 2] / iboC[i]
            }
        }
        if (Renderer.USE_NEW_SHIT && lightType != 2) {
            for (i in 0 until vboIdx) {
                currBuf!!.putP(
                    pvbo[i * 3 + 0].toFloat(),
                    pvbo[i * 3 + 1].toFloat(),
                    pvbo[i * 3 + 2].toFloat()
                )
                if (emitColor) {
                    if (hasColor) currBuf!!.putC(
                        cvbo[i * 4 + 0],
                        cvbo[i * 4 + 1],
                        cvbo[i * 4 + 2],
                        cvbo[i * 4 + 3]
                    ) else currBuf!!.putC(
                        currCol!!.r,
                        currCol!!.g,
                        currCol!!.b,
                        currCol!!.a
                    )
                }
                if (lightType == 1) currBuf!!.putN(
                    nvbo[i * 3 + 0].toFloat(),
                    nvbo[i * 3 + 1].toFloat(),
                    nvbo[i * 3 + 2].toFloat()
                )
                if (isTex) currBuf!!.putT(
                    tvbo[i * 2 + 0].toFloat(),
                    tvbo[i * 2 + 1].toFloat()
                )
            }
        }
        if (!Renderer.USE_NEW_SHIT) {
            if (lightType == 1) ibo.forEach {
                emitVert(
                    it,
                    nvbo[it * 3 + 0],
                    nvbo[it * 3 + 1],
                    nvbo[it * 3 + 2]
                )
            }
            tess.draw()
        }
    }

    fun addVert(x: Double, y: Double, z: Double): Int {
        pvbo[vboIdx * 3 + 0] = x - getRenderX()
        pvbo[vboIdx * 3 + 1] = y - getRenderY()
        pvbo[vboIdx * 3 + 2] = z - getRenderZ()
        return vboIdx++
    }
    fun addVert(x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, a: Float): Int {
        pvbo[vboIdx * 3 + 0] = x - getRenderX()
        pvbo[vboIdx * 3 + 1] = y - getRenderY()
        pvbo[vboIdx * 3 + 2] = z - getRenderZ()
        cvbo[vboIdx * 4 + 0] = r
        cvbo[vboIdx * 4 + 1] = g
        cvbo[vboIdx * 4 + 2] = b
        cvbo[vboIdx * 4 + 3] = a
        return vboIdx++
    }
    fun addVert(x: Double, y: Double, z: Double, u: Double, v: Double): Int {
        pvbo[vboIdx * 3 + 0] = x - getRenderX()
        pvbo[vboIdx * 3 + 1] = y - getRenderY()
        pvbo[vboIdx * 3 + 2] = z - getRenderZ()
        tvbo[vboIdx * 2 + 0] = u
        tvbo[vboIdx * 2 + 1] = v
        return vboIdx++
    }
    fun addVert(x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, a: Float, u: Double, v: Double): Int {
        pvbo[vboIdx * 3 + 0] = x - getRenderX()
        pvbo[vboIdx * 3 + 1] = y - getRenderY()
        pvbo[vboIdx * 3 + 2] = z - getRenderZ()
        cvbo[vboIdx * 4 + 0] = r
        cvbo[vboIdx * 4 + 1] = g
        cvbo[vboIdx * 4 + 2] = b
        cvbo[vboIdx * 4 + 3] = a
        tvbo[vboIdx * 2 + 0] = u
        tvbo[vboIdx * 2 + 1] = v
        return vboIdx++
    }

    fun addVertRaw(x: Double, y: Double, z: Double): Int {
        pvbo[vboIdx * 3 + 0] = x
        pvbo[vboIdx * 3 + 1] = y
        pvbo[vboIdx * 3 + 2] = z
        return vboIdx++
    }
    fun addVertRaw(x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, a: Float): Int {
        pvbo[vboIdx * 3 + 0] = x
        pvbo[vboIdx * 3 + 1] = y
        pvbo[vboIdx * 3 + 2] = z
        cvbo[vboIdx * 4 + 0] = r
        cvbo[vboIdx * 4 + 1] = g
        cvbo[vboIdx * 4 + 2] = b
        cvbo[vboIdx * 4 + 3] = a
        return vboIdx++
    }
    fun addVertRaw(x: Double, y: Double, z: Double, u: Double, v: Double): Int {
        pvbo[vboIdx * 3 + 0] = x
        pvbo[vboIdx * 3 + 1] = y
        pvbo[vboIdx * 3 + 2] = z
        tvbo[vboIdx * 2 + 0] = u
        tvbo[vboIdx * 2 + 1] = v
        return vboIdx++
    }
    fun addVertRaw(x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, a: Float, u: Double, v: Double): Int {
        pvbo[vboIdx * 3 + 0] = x
        pvbo[vboIdx * 3 + 1] = y
        pvbo[vboIdx * 3 + 2] = z
        cvbo[vboIdx * 4 + 0] = r
        cvbo[vboIdx * 4 + 1] = g
        cvbo[vboIdx * 4 + 2] = b
        cvbo[vboIdx * 4 + 3] = a
        tvbo[vboIdx * 2 + 0] = u
        tvbo[vboIdx * 2 + 1] = v
        return vboIdx++
    }

    fun emitVert(idx: Int) {
        return emitVert(idx, 0.0, 1.0, 0.0)
    }
    fun emitVert(idx: Int, nx: Double, ny: Double, nz: Double) {
        if (Renderer.USE_NEW_SHIT) index(idx)
        else {
            worldRen.pos(
                pvbo[idx * 3 + 0],
                pvbo[idx * 3 + 1],
                pvbo[idx * 3 + 2]
            )
            if (isTex) worldRen.tex(
                tvbo[idx * 2 + 0],
                tvbo[idx * 2 + 1]
            )
            if (emitColor) {
                if (hasColor) worldRen.color(
                    cvbo[idx * 4 + 0],
                    cvbo[idx * 4 + 1],
                    cvbo[idx * 4 + 2],
                    cvbo[idx * 4 + 3]
                )
                else worldRen.color(
                    currCol!!.r,
                    currCol!!.g,
                    currCol!!.b,
                    currCol!!.a
                )
            }
            if (lightType != 0) worldRen.normal(nx.toFloat(), ny.toFloat(), nz.toFloat())
            worldRen.endVertex()
        }
        emitC++
    }

    fun pos(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double) {
        pos(x, y, z, nx, ny, nz, currCol!!.r, currCol!!.g, currCol!!.b, currCol!!.a)
    }
    fun pos(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double, r: Float, g: Float, b: Float, a: Float) {
        if (Renderer.USE_NEW_SHIT) {
            currBuf!!.putP((x - getRenderX()).toFloat(), (y - getRenderY()).toFloat(), (z - getRenderZ()).toFloat())
            if (emitColor) currBuf!!.putC(r, g, b, a)
            if (lightType != 0) currBuf!!.putN(nx.toFloat(), ny.toFloat(), nz.toFloat())
            index(emitC)
        } else {
            worldRen.pos(x - getRenderX(), y - getRenderY(), z - getRenderZ())
            if (emitColor) worldRen.color(r, g, b, a)
            if (lightType != 0) worldRen.normal(nx.toFloat(), ny.toFloat(), nz.toFloat())
            worldRen.endVertex()
        }
        emitC++
    }
    fun pos(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double, u: Double, v: Double) {
        pos(x, y, z, nx, ny, nz, u, v, currCol!!.r, currCol!!.g, currCol!!.b, currCol!!.a)
    }
    fun pos(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double, u: Double, v: Double, r: Float, g: Float, b: Float, a: Float) {
        if (Renderer.USE_NEW_SHIT) {
            currBuf!!.putP((x - getRenderX()).toFloat(), (y - getRenderY()).toFloat(), (z - getRenderZ()).toFloat())
            if (emitColor) currBuf!!.putC(r, g, b, a)
            if (lightType != 0) currBuf!!.putN(nx.toFloat(), ny.toFloat(), nz.toFloat())
            if (isTex) currBuf!!.putT(u.toFloat(), v.toFloat())
            index(emitC)
        } else {
            worldRen.pos(x - getRenderX(), y - getRenderY(), z - getRenderZ())
            if (isTex) worldRen.tex(u, v)
            if (emitColor) worldRen.color(r, g, b, a)
            if (lightType != 0) worldRen.normal(nx.toFloat(), ny.toFloat(), nz.toFloat())
            worldRen.endVertex()
        }
        emitC++
    }

    fun posRaw(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double) {
        posRaw(x, y, z, nx, ny, nz, currCol!!.r, currCol!!.g, currCol!!.b, currCol!!.a)
    }
    fun posRaw(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double, r: Float, g: Float, b: Float, a: Float) {
        if (Renderer.USE_NEW_SHIT) {
            currBuf!!.putP(x.toFloat(), y.toFloat(), z.toFloat())
            if (emitColor) currBuf!!.putC(r, g, b, a)
            if (lightType != 0) currBuf!!.putN(nx.toFloat(), ny.toFloat(), nz.toFloat())
            index(emitC)
        } else {
            worldRen.pos(x, y, z)
            if (emitColor) worldRen.color(r, g, b, a)
            if (lightType != 0) worldRen.normal(nx.toFloat(), ny.toFloat(), nz.toFloat())
            worldRen.endVertex()
        }
        emitC++
    }
    fun posRaw(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double, u: Double, v: Double) {
        posRaw(x, y, z, nx, ny, nz, u, v, currCol!!.r, currCol!!.g, currCol!!.b, currCol!!.a)
    }
    fun posRaw(x: Double, y: Double, z: Double, nx: Double, ny: Double, nz: Double, u: Double, v: Double, r: Float, g: Float, b: Float, a: Float) {
        if (Renderer.USE_NEW_SHIT) {
            currBuf!!.putP(x.toFloat(), y.toFloat(), z.toFloat())
            if (emitColor) currBuf!!.putC(r, g, b, a)
            if (lightType != 0) currBuf!!.putN(nx.toFloat(), ny.toFloat(), nz.toFloat())
            if (isTex) currBuf!!.putT(u.toFloat(), v.toFloat())
            index(emitC)
        } else {
            worldRen.pos(x, y, z)
            if (isTex) worldRen.tex(u, v)
            if (emitColor) worldRen.color(r, g, b, a)
            if (lightType != 0) worldRen.normal(nx.toFloat(), ny.toFloat(), nz.toFloat())
            worldRen.endVertex()
        }
        emitC++
    }

    fun tryPosRaw(idx: Int, nx: Double, ny: Double, nz: Double) {
        if (hasColor) {
            if (isTex) posRaw(
                pvbo[idx * 3 + 0],
                pvbo[idx * 3 + 1],
                pvbo[idx * 3 + 2],
                nx, ny, nz,
                tvbo[idx * 2 + 0],
                tvbo[idx * 2 + 1],
                cvbo[idx * 4 + 0],
                cvbo[idx * 4 + 1],
                cvbo[idx * 4 + 2],
                cvbo[idx * 4 + 3]
            )
            else posRaw(
                pvbo[idx * 3 + 0],
                pvbo[idx * 3 + 1],
                pvbo[idx * 3 + 2],
                nx, ny, nz,
                cvbo[idx * 4 + 0],
                cvbo[idx * 4 + 1],
                cvbo[idx * 4 + 2],
                cvbo[idx * 4 + 3]
            )
        } else {
            if (isTex) posRaw(
                pvbo[idx * 3 + 0],
                pvbo[idx * 3 + 1],
                pvbo[idx * 3 + 2],
                nx, ny, nz,
                tvbo[idx * 2 + 0],
                tvbo[idx * 2 + 1]
            )
            else posRaw(
                pvbo[idx * 3 + 0],
                pvbo[idx * 3 + 1],
                pvbo[idx * 3 + 2],
                nx, ny, nz
            )
        }
    }

    fun addTri(p1: Int, p2: Int, p3: Int) {
        val x1 = pvbo[p1 * 3 + 0]
        val y1 = pvbo[p1 * 3 + 1]
        val z1 = pvbo[p1 * 3 + 2]
        val x2 = pvbo[p2 * 3 + 0]
        val y2 = pvbo[p2 * 3 + 1]
        val z2 = pvbo[p2 * 3 + 2]
        val x3 = pvbo[p3 * 3 + 0]
        val y3 = pvbo[p3 * 3 + 1]
        val z3 = pvbo[p3 * 3 + 2]

        if (lightType == 0 || (lightType == 1 && Renderer.USE_NEW_SHIT)) {
            emitVert(p1)
            emitVert(p2)
            emitVert(p3)
        }
        if (lightType == 1 && !Renderer.USE_NEW_SHIT) {
            ibo[iboI++] = p1
            ibo[iboI++] = p2
            ibo[iboI++] = p3
        }
        if (lightType != 0) {
            val i1 = x1 - x2
            val j1 = y1 - y2
            val k1 = z1 - z2
            val i2 = x1 - x3
            val j2 = y1 - y3
            val k2 = z1 - z3
            var nx = j1 * k2 - k1 * j2
            var ny = k1 * i2 - i1 * k2
            var nz = i1 * j2 - j1 * i2
            val f = 1.0 / sqrt(nx * nx + ny * ny + nz * nz)
            nx *= f
            ny *= f
            nz *= f
            if (lightType == 1) {
                nvbo[p1 * 3 + 0] += nx
                nvbo[p1 * 3 + 1] += ny
                nvbo[p1 * 3 + 2] += nz
                iboC[p1]++

                nvbo[p2 * 3 + 0] += nx
                nvbo[p2 * 3 + 1] += ny
                nvbo[p2 * 3 + 2] += nz
                iboC[p2]++

                nvbo[p3 * 3 + 0] += nx
                nvbo[p3 * 3 + 1] += ny
                nvbo[p3 * 3 + 2] += nz
                iboC[p3]++
            }
            if (lightType == 2) {
                tryPosRaw(p1, nx, ny, nz)
                tryPosRaw(p2, nx, ny, nz)
                tryPosRaw(p3, nx, ny, nz)
            }
        }
    }
}
operator fun <E> List<E>.component6() = get(5)
operator fun <E> List<E>.component7() = get(6)
operator fun <E> List<E>.component8() = get(7)
operator fun <E> List<E>.component9() = get(8)
operator fun <E> List<E>.component10() = get(9)
operator fun <E> List<E>.component11() = get(10)
operator fun <E> List<E>.component12() = get(11)
operator fun <E> List<E>.component13() = get(12)