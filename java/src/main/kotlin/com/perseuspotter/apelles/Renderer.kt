package com.perseuspotter.apelles

import com.google.common.collect.TreeMultiset
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.Point
import com.perseuspotter.apelles.state.Color
import com.perseuspotter.apelles.state.GlState
import com.perseuspotter.apelles.state.Thingamabob
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL31
import org.lwjgl.opengl.GLContext
import kotlin.math.*

object Renderer {
    private val texturedOpaque = TreeMultiset.create<Thingamabob>()
    private val texturedTranslucent = TreeMultiset.create<Thingamabob>()
    private val opaque = TreeMultiset.create<Thingamabob>()
    private val translucent = TreeMultiset.create<Thingamabob>()
    private var empty = true

    fun addTexturedThing(thing: Thingamabob) {
        if (thing.color.a == 1f) texturedOpaque.add(thing)
        else if (thing.color.a > 0f) texturedTranslucent.add(thing)
        else return
        empty = false
    }

    fun addThing(thing: Thingamabob) {
        if (thing.color.a == 1f) opaque.add(thing)
        else if (thing.color.a > 0f) translucent.add(thing)
        else return
        empty = false
    }

    fun addLine(
        color: Long,
        points: Array<DoubleArray>,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addLine(Color(color), points, lw, lighting, phase, smooth, cull, chroma)
    fun addLine(
        color: FloatArray,
        points: Array<DoubleArray>,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addLine(Color(color), points, lw, lighting, phase, smooth, cull, chroma)
    fun addLine(
        color: Color,
        points: Array<DoubleArray>,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        val params = DoubleArray(points.size * 3)
        points.forEachIndexed { i, v ->
            params[i * 3 + 0] = v[0]
            params[i * 3 + 1] = v[1]
            params[i * 3 + 2] = v[2]
        }
        addThing(
            Thingamabob(
                Thingamabob.Type.Line,
                params,
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                chroma
            )
        )
    }

    fun addAABBO(
        color: Long,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addAABBO(Color(color), x1, y1, z1, x2, y2, z2, lw, lighting, phase, smooth, cull, chroma)
    fun addAABBO(
        color: FloatArray,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addAABBO(Color(color), x1, y1, z1, x2, y2, z2, lw, lighting, phase, smooth, cull, chroma)
    fun addAABBO(
        color: Color,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.AABBO,
                doubleArrayOf(x1, y1, z1, x2, y2, z2),
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                chroma
            )
        )
    }

    fun addAABBF(
        color: Long,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addAABBF(Color(color), x1, y1, z1, x2, y2, z2, lighting, phase, cull, chroma)
    fun addAABBF(
        color: FloatArray,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addAABBF(Color(color), x1, y1, z1, x2, y2, z2, lighting, phase, cull, chroma)
    fun addAABBF(
        color: Color,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.AABBF,
                doubleArrayOf(x1, y1, z1, x2, y2, z2),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma
            )
        )
    }

    private val beaconBeamTexture = ResourceLocation("textures/entity/beacon_beam.png")
    fun addBeacon(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addBeacon(Color(color), x, y, z, h, lighting, phase, cull, chroma)
    fun addBeacon(
        color: FloatArray,
        x: Double,
        y: Double,
        z: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addBeacon(Color(color), x, y, z, h, lighting, phase, cull, chroma)
    fun addBeacon(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        val (sx, sy, sz, s) = Geometry.rescale(x, y, z)
        var y1 = sy
        var y2 = sy + h * s
        var inBounds = false
        for (i in 0..3) {
            val (p1, p2) = Frustum.clip(
                Point(
                    sx + (if (i and 1 == 0) -0.3 else 0.3) * s,
                    y1,
                    sz + (if (i and 1 == 0) -0.3 else 0.3) * s
                ),
                Point(
                    sx + (if (i and 1 == 0) -0.3 else 0.3) * s,
                    y2,
                    sz + (if (i and 1 == 0) -0.3 else 0.3) * s
                )
            )
            if (p1 == null || p2 == null) continue
            inBounds = true
            y1 = min(y1, min(p1.y, p2.y))
            y2 = max(y2, max(p1.y, p2.y))
        }
        if (!inBounds) return
        val color2 = Color(color.r, color.g, color.b, color.a / 4f)
        addTexturedThing(
            Thingamabob(
                Thingamabob.Type.BeaconI,
                doubleArrayOf(sx, y1, sz, y2 - y1, s),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma,
                beaconBeamTexture
            )
        )
        addTexturedThing(
            Thingamabob(
                Thingamabob.Type.BeaconO,
                doubleArrayOf(sx, y1, sz, y2 - y1, s),
                color2,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma,
                beaconBeamTexture
            )
        )
        addThing(
            Thingamabob(
                Thingamabob.Type.BeaconTI,
                doubleArrayOf(sx, y1, sz, y2 - y1, s),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma
            )
        )
        addThing(
            Thingamabob(
                Thingamabob.Type.BeaconTO,
                doubleArrayOf(sx, y1, sz, y2 - y1, s),
                color2,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma
            )
        )
    }

    fun addCircle(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        segments: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addCircle(Color(color), x, y, z, r, segments, lw, lighting, phase, smooth, cull, chroma)
    fun addCircle(
        color: FloatArray,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        segments: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addCircle(Color(color), x, y, z, r, segments, lw, lighting, phase, smooth, cull, chroma)
    fun addCircle(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        segments: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        val points = DoubleArray((segments + 1) * 3)
        for (i in 0 until segments) {
            val t = 2.0 * PI * i / segments
            points[i * 3 + 0] = x + cos(t) * r
            points[i * 3 + 1] = y
            points[i * 3 + 2] = z + sin(t) * r
        }
        // something something floating point cos(0) ~= cos(2pi)
        points[segments * 3 + 0] = points[0]
        points[segments * 3 + 1] = points[1]
        points[segments * 3 + 2] = points[2]
        addThing(
            Thingamabob(
                Thingamabob.Type.Line,
                points,
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                chroma
            )
        )
    }

    fun addIcosphere(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        divisions: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addIcosphere(Color(color), x, y, z, r, divisions, lighting, phase, cull, chroma)
    fun addIcosphere(
        color: FloatArray,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        divisions: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addIcosphere(Color(color), x, y, z, r, divisions, lighting, phase, cull, chroma)
    fun addIcosphere(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        divisions: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.Icosphere,
                doubleArrayOf(x, y, z, r, divisions.toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma
            )
        )
    }

    fun addPyramidO(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        n: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addPyramidO(Color(color), x, y, z, r, h, n, lw, lighting, phase, smooth, cull, chroma)
    fun addPyramidO(
        color: FloatArray,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        n: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addPyramidO(Color(color), x, y, z, r, h, n, lw, lighting, phase, smooth, cull, chroma)
    fun addPyramidO(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        n: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.PyramidO,
                doubleArrayOf(x, y, z, r, h, n.toDouble()),
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                chroma
            )
        )
    }

    fun addPyramidF(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        n: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addPyramidF(Color(color), x, y, z, r, h, n, lighting, phase, cull, chroma)
    fun addPyramidF(
        color: FloatArray,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        n: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addPyramidF(Color(color), x, y, z, r, h, n, lighting, phase, cull, chroma)
    fun addPyramidF(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        n: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.PyramidF,
                doubleArrayOf(x, y, z, r, h, n.toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma
            )
        )
    }

    fun addVertCylinder(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        segments: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addVertCylinder(Color(color), x, y, z, r, h, segments, lighting, phase, cull, chroma)
    fun addVertCylinder(
        color: FloatArray,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        segments: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addVertCylinder(Color(color), x, y, z, r, h, segments, lighting, phase, cull, chroma)
    fun addVertCylinder(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        segments: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.VertCylinderR,
                doubleArrayOf(x, y, z, r, h, segments.toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma
            )
        )
        addThing(
            Thingamabob(
                Thingamabob.Type.VertCylinderC,
                doubleArrayOf(x, y, z, r, h, segments.toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma
            )
        )
    }

    fun addOctahedronO(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addOctahedronO(Color(color), x, y, z, w, h, lw, lighting, phase, smooth, cull, chroma)
    fun addOctahedronO(
        color: FloatArray,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addOctahedronO(Color(color), x, y, z, w, h, lw, lighting, phase, smooth, cull, chroma)
    fun addOctahedronO(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.OctahedronO,
                doubleArrayOf(x, y, z, w, h),
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                chroma
            )
        )
    }

    fun addOctahedronF(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addOctahedronF(Color(color), x, y, z, w, h, lighting, phase, cull, chroma)
    fun addOctahedronF(
        color: FloatArray,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) = addOctahedronF(Color(color), x, y, z, w, h, lighting, phase, cull, chroma)
    fun addOctahedronF(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        chroma: Boolean
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.OctahedronF,
                doubleArrayOf(x, y, z, w, h),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                chroma
            )
        )
    }

    @JvmField
    var USE_NEW_SHIT: Boolean = false
    private var checked = false
    fun render(pt: Double) {
        if (!checked) {
            val cap = GLContext.getCapabilities()
            USE_NEW_SHIT =
                cap.GL_ARB_vertex_buffer_object && cap.GL_NV_primitive_restart && cap.GL_ARB_vertex_array_object && cap.GL_ARB_vertex_shader
            checked = true
        }
        if (empty) return

        GlState.reset()
        GlState.push()
        glPushMatrix()
        glTranslated(-Geometry.getRenderX(), -Geometry.getRenderY(), -Geometry.getRenderZ())

        glDisable(GL_ALPHA_TEST)
        glEnable(GL_CULL_FACE)
        glFrontFace(GL_CCW)
        glEnable(GL_DEPTH_TEST)
        glDisable(GL_FOG)

        glShadeModel(GL_SMOOTH)
        glEnable(GL_LIGHT0)
        glEnable(GL_LIGHT1)
        glEnable(GL_COLOR_MATERIAL)
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE)

        if (USE_NEW_SHIT) {
            glEnable(GL31.GL_PRIMITIVE_RESTART)
            // GL31.glPrimitiveRestartIndex(Geometry.PRIMITIVE_RESTART_INDEX)
            glEnableClientState(GL_VERTEX_ARRAY)
        }

        glDisable(GL_BLEND)
        glDepthMask(true)

        glEnable(GL_TEXTURE_2D)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT.toFloat())

        if (USE_NEW_SHIT) {
            Geometry.bindBufGroup(0)
            texturedOpaque.forEach {
                Geometry.allocate(
                    it.getVBOGroupingId(),
                    it.getVertexCount(),
                    it.getIndicesCount() + 1,
                    true,
                    it.lighting > 0,
                    true,
                    it.getDrawMode(),
                    it
                )
            }
            Geometry.prepare()
        }
        texturedOpaque.forEach {
            if (USE_NEW_SHIT) Geometry.bind(it)
            it.render(pt)
        }
        texturedOpaque.clear()
        if (USE_NEW_SHIT) Geometry.render(pt)

        glEnable(GL_BLEND)
        OpenGlHelper.glBlendFunc(770, 771, 1, 771)
        glDepthMask(false)

        if (USE_NEW_SHIT) {
            Geometry.bindBufGroup(1)
            texturedTranslucent.forEach {
                Geometry.allocate(
                    it.getVBOGroupingId(),
                    it.getVertexCount(),
                    it.getIndicesCount() + 1,
                    true,
                    it.lighting > 0,
                    true,
                    it.getDrawMode(),
                    it
                )
            }
            Geometry.prepare()
        }
        texturedTranslucent.forEach {
            if (USE_NEW_SHIT) Geometry.bind(it)
            it.render(pt)
        }
        texturedTranslucent.clear()
        if (USE_NEW_SHIT) Geometry.render(pt)
        glDisable(GL_TEXTURE_2D)

        glDisable(GL_BLEND)
        glDepthMask(true)

        if (USE_NEW_SHIT) {
            Geometry.bindBufGroup(2)
            opaque.forEach {
                Geometry.allocate(
                    it.getVBOGroupingId(),
                    it.getVertexCount(),
                    it.getIndicesCount() + 1,
                    true,
                    it.lighting > 0,
                    false,
                    it.getDrawMode(),
                    it
                )
            }
            Geometry.prepare()
        }
        opaque.forEach {
            if (USE_NEW_SHIT) Geometry.bind(it)
            it.render(pt)
        }
        opaque.clear()
        if (USE_NEW_SHIT) Geometry.render(pt)

        glEnable(GL_BLEND)
        // OpenGlHelper.glBlendFunc(770, 771, 1, 771)
        glDepthMask(false)

        if (USE_NEW_SHIT) {
            Geometry.bindBufGroup(3)
            translucent.forEach {
                Geometry.allocate(
                    it.getVBOGroupingId(),
                    it.getVertexCount(),
                    it.getIndicesCount() + 1,
                    true,
                    it.lighting > 0,
                    false,
                    it.getDrawMode(),
                    it
                )
            }
            Geometry.prepare()
        }
        translucent.forEach {
            if (USE_NEW_SHIT) Geometry.bind(it)
            it.render(pt)
        }
        translucent.clear()
        if (USE_NEW_SHIT) {
            Geometry.render(pt)

            GlState.bindShader(0)
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
            glDisable(GL31.GL_PRIMITIVE_RESTART)
            glDisableClientState(GL_VERTEX_ARRAY)
            GlState.setColorArray(false)
            GlState.setNormalArray(false)
            GlState.setTexArray(false)
        }

        glPopMatrix()
        GlState.pop()
        empty = true
    }
}