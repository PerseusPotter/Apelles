package com.perseuspotter.apelles

import com.perseuspotter.apelles.depression.ChromaShader
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.Point
import com.perseuspotter.apelles.outline.EntityOutlineRenderer
import com.perseuspotter.apelles.state.Color
import com.perseuspotter.apelles.state.GlState
import com.perseuspotter.apelles.state.Thingamabob
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL31
import org.lwjgl.opengl.GLContext
import kotlin.math.*

object Renderer {
    private val texturedOpaque = mutableListOf<Thingamabob>()
    private val texturedTranslucent = mutableListOf<Thingamabob>()
    private val opaque = mutableListOf<Thingamabob>()
    private val translucent = mutableListOf<Thingamabob>()

    fun addTexturedThing(thing: Thingamabob) {
        if (thing.color.a == 1f) texturedOpaque.add(thing)
        else if (thing.color.a > 0f) texturedTranslucent.add(thing)
    }

    fun addThing(thing: Thingamabob) {
        if (thing.color.a == 1f) opaque.add(thing)
        else if (thing.color.a > 0f) translucent.add(thing)
    }

    fun addPrimitive(
        color: Long,
        mode: Int,
        points: List<List<Double>>,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addPrimitive(Color(color), mode, points, lw, lighting, phase, smooth, cull, backfaceCull, chroma)
    fun addPrimitive(
        color: List<Double>,
        mode: Int,
        points: List<List<Double>>,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addPrimitive(Color(color), mode, points, lw, lighting, phase, smooth, cull, backfaceCull, chroma)
    fun addPrimitive(
        color: Color,
        mode: Int,
        points: List<List<Double>>,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) {
        val params = points.flatten().toMutableList()
        params.add(0, mode.toDouble())
        addThing(
            Thingamabob(
                Thingamabob.Type.Primitive,
                params,
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                backfaceCull,
                chroma
            )
        )
    }

    fun addBoxO(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        wx: Double,
        h: Double,
        wz: Double,
        centered: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addAABBO(Color(color), if (centered) x - wx * 0.5 else x, y, if (centered) z - wz * 0.5 else z, if (centered) x + wx * 0.5 else x + wx, y + h, if (centered) z + wz * 0.5 else z + wz, lw, lighting, phase, smooth, cull, chroma)
    fun addBoxO(
        color: List<Double>,
        x: Double,
        y: Double,
        z: Double,
        wx: Double,
        h: Double,
        wz: Double,
        centered: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addAABBO(Color(color), if (centered) x - wx * 0.5 else x, y, if (centered) z - wz * 0.5 else z, if (centered) x + wx * 0.5 else x + wx, y + h, if (centered) z + wz * 0.5 else z + wz, lw, lighting, phase, smooth, cull, chroma)
    fun addBoxO(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        wx: Double,
        h: Double,
        wz: Double,
        centered: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addAABBO(color, if (centered) x - wx * 0.5 else x, y, if (centered) z - wz * 0.5 else z, if (centered) x + wx * 0.5 else x + wx, y + h, if (centered) z + wz * 0.5 else z + wz, lw, lighting, phase, smooth, cull, chroma)
    fun addAABBOM(
        color: Long,
        aabb: AxisAlignedBB,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addAABBO(color, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lw, lighting, phase, smooth, cull, chroma)
    fun addAABBOM(
        color: List<Double>,
        aabb: AxisAlignedBB,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addAABBO(color, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lw, lighting, phase, smooth, cull, chroma)
    fun addAABBOM(
        color: Color,
        aabb: AxisAlignedBB,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addAABBO(color, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lw, lighting, phase, smooth, cull, chroma)
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
        chroma: Int
    ) = addAABBO(Color(color), x1, y1, z1, x2, y2, z2, lw, lighting, phase, smooth, cull, chroma)
    fun addAABBO(
        color: List<Double>,
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
        chroma: Int
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
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.AABBO,
                listOf(x1, y1, z1, x2, y2, z2),
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                true,
                chroma
            )
        )
    }

    fun addBoxF(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        wx: Double,
        h: Double,
        wz: Double,
        centered: Boolean,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBF(Color(color), if (centered) x - wx * 0.5 else x, y, if (centered) z - wz * 0.5 else z, if (centered) x + wx * 0.5 else x + wx, y + h, if (centered) z + wz * 0.5 else z + wz, lighting, phase, cull, backfaceCull, chroma)
    fun addBoxF(
        color: List<Double>,
        x: Double,
        y: Double,
        z: Double,
        wx: Double,
        h: Double,
        wz: Double,
        centered: Boolean,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBF(Color(color), if (centered) x - wx * 0.5 else x, y, if (centered) z - wz * 0.5 else z, if (centered) x + wx * 0.5 else x + wx, y + h, if (centered) z + wz * 0.5 else z + wz, lighting, phase, cull, backfaceCull, chroma)
    fun addBoxF(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        wx: Double,
        h: Double,
        wz: Double,
        centered: Boolean,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBF(color, if (centered) x - wx * 0.5 else x, y, if (centered) z - wz * 0.5 else z, if (centered) x + wx * 0.5 else x + wx, y + h, if (centered) z + wz * 0.5 else z + wz, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBFM(
        color: Long,
        aabb: AxisAlignedBB,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBF(color, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBFM(
        color: List<Double>,
        aabb: AxisAlignedBB,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBF(color, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBFM(
        color: Color,
        aabb: AxisAlignedBB,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBF(color, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lighting, phase, cull, backfaceCull, chroma)
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
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBF(Color(color), x1, y1, z1, x2, y2, z2, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBF(
        color: List<Double>,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBF(Color(color), x1, y1, z1, x2, y2, z2, lighting, phase, cull, backfaceCull, chroma)
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
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.AABBF,
                listOf(x1, y1, z1, x2, y2, z2),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
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
        backfaceCull: Boolean,
        chroma: Int
    ) = addBeacon(Color(color), x, y, z, h, lighting, phase, cull, backfaceCull, chroma)
    fun addBeacon(
        color: List<Double>,
        x: Double,
        y: Double,
        z: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addBeacon(Color(color), x, y, z, h, lighting, phase, cull, backfaceCull, chroma)
    fun addBeacon(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
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
                listOf(sx, y1, sz, y2 - y1, s),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma,
                beaconBeamTexture
            )
        )
        addTexturedThing(
            Thingamabob(
                Thingamabob.Type.BeaconO,
                listOf(sx, y1, sz, y2 - y1, s),
                color2,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma,
                beaconBeamTexture
            )
        )
        addThing(
            Thingamabob(
                Thingamabob.Type.BeaconTI,
                listOf(sx, y1, sz, y2 - y1, s),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma
            )
        )
        addThing(
            Thingamabob(
                Thingamabob.Type.BeaconTO,
                listOf(sx, y1, sz, y2 - y1, s),
                color2,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
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
        chroma: Int
    ) = addCircle(Color(color), x, y, z, r, segments, lw, lighting, phase, smooth, cull, chroma)
    fun addCircle(
        color: List<Double>,
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
        chroma: Int
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
        chroma: Int
    ) {
        val points = mutableListOf<Double>()
        points.add(GL_LINE_STRIP.toDouble())
        for (i in 0 until segments) {
            val t = 2.0 * PI * i / segments
            points.add(x + cos(t) * r)
            points.add(y)
            points.add(z + sin(t) * r)
        }
        // something something floating point cos(0) ~= cos(2pi)
        points.add(points[1])
        points.add(points[2])
        points.add(points[3])
        addThing(
            Thingamabob(
                Thingamabob.Type.Primitive,
                points,
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                true,
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
        backfaceCull: Boolean,
        chroma: Int
    ) = addIcosphere(Color(color), x, y, z, r, divisions, lighting, phase, cull, backfaceCull, chroma)
    fun addIcosphere(
        color: List<Double>,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        divisions: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addIcosphere(Color(color), x, y, z, r, divisions, lighting, phase, cull, backfaceCull, chroma)
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
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.Icosphere,
                listOf(x, y, z, r, divisions.toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
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
        chroma: Int
    ) = addPyramidO(Color(color), x, y, z, r, h, n, lw, lighting, phase, smooth, cull, chroma)
    fun addPyramidO(
        color: List<Double>,
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
        chroma: Int
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
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.PyramidO,
                listOf(x, y, z, r, h, n.toDouble()),
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                true,
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
        backfaceCull: Boolean,
        chroma: Int
    ) = addPyramidF(Color(color), x, y, z, r, h, n, lighting, phase, cull, backfaceCull, chroma)
    fun addPyramidF(
        color: List<Double>,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        n: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addPyramidF(Color(color), x, y, z, r, h, n, lighting, phase, cull, backfaceCull, chroma)
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
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.PyramidF,
                listOf(x, y, z, r, h, n.toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
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
        backfaceCull: Boolean,
        chroma: Int
    ) = addVertCylinder(Color(color), x, y, z, r, h, segments, lighting, phase, cull, backfaceCull, chroma)
    fun addVertCylinder(
        color: List<Double>,
        x: Double,
        y: Double,
        z: Double,
        r: Double,
        h: Double,
        segments: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addVertCylinder(Color(color), x, y, z, r, h, segments, lighting, phase, cull, backfaceCull, chroma)
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
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.VertCylinderR,
                listOf(x, y, z, r, h, segments.toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma
            )
        )
        addThing(
            Thingamabob(
                Thingamabob.Type.VertCylinderC,
                listOf(x, y, z, r, h, segments.toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
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
        chroma: Int
    ) = addOctahedronO(Color(color), x, y, z, w, h, lw, lighting, phase, smooth, cull, chroma)
    fun addOctahedronO(
        color: List<Double>,
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
        chroma: Int
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
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.OctahedronO,
                listOf(x, y, z, w, h),
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                true,
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
        backfaceCull: Boolean,
        chroma: Int
    ) = addOctahedronF(Color(color), x, y, z, w, h, lighting, phase, cull, backfaceCull, chroma)
    fun addOctahedronF(
        color: List<Double>,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addOctahedronF(Color(color), x, y, z, w, h, lighting, phase, cull, backfaceCull, chroma)
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
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.OctahedronF,
                listOf(x, y, z, w, h),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma
            )
        )
    }

    fun addStraightStairO(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStraightStairO(Color(color), x, y, z, type, lw, lighting, phase, smooth, cull, chroma)
    fun addStraightStairO(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStraightStairO(Color(color), x, y, z, type, lw, lighting, phase, smooth, cull, chroma)
    fun addStraightStairO(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.StairStraightO,
                listOf(x + 0.5, y + 0.5, z + 0.5, type.toDouble()),
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                true,
                chroma
            )
        )
    }

    fun addStraightStairF(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStraightStairF(Color(color), x, y, z, type, lighting, phase, cull, backfaceCull, chroma)
    fun addStraightStairF(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStraightStairF(Color(color), x, y, z, type, lighting, phase, cull, backfaceCull, chroma)
    fun addStraightStairF(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.StairStraightF,
                listOf(x + 0.5, y + 0.5, z + 0.5, type.toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma
            )
        )
    }

    // alternatively: if (left) type xor 2 xor ((type and 2 xor ((type.inv() and 4) shr 1)) shr 1) else type
    val stairTypeTransform = intArrayOf(3, 2, 0, 1, 6, 7, 5, 4)
    fun addInnerStairO(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addInnerStairO(Color(color), x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma)
    fun addInnerStairO(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addInnerStairO(Color(color), x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma)
    fun addInnerStairO(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.StairInnerO,
                listOf(x + 0.5, y + 0.5, z + 0.5, (if (left) stairTypeTransform[type] else type).toDouble()),
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                true,
                chroma
            )
        )
    }

    fun addInnerStairF(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addInnerStairF(Color(color), x, y, z, type, left, lighting, phase, cull, backfaceCull, chroma)
    fun addInnerStairF(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addInnerStairF(Color(color), x, y, z, type, left, lighting, phase, cull, backfaceCull, chroma)
    fun addInnerStairF(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.StairInnerF,
                listOf(x + 0.5, y + 0.5, z + 0.5, (if (left) stairTypeTransform[type] else type).toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma
            )
        )
    }

    fun addOuterStairO(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addOuterStairO(Color(color), x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma)
    fun addOuterStairO(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addOuterStairO(Color(color), x, y, z, type, left, lw, lighting, phase, smooth, cull, chroma)
    fun addOuterStairO(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.StairOuterO,
                listOf(x + 0.5, y + 0.5, z + 0.5, (if (left) stairTypeTransform[type] else type).toDouble()),
                color,
                lw.toFloat(),
                lighting,
                phase,
                smooth,
                cull,
                true,
                chroma
            )
        )
    }

    fun addOuterStairF(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addOuterStairF(Color(color), x, y, z, type, left, lighting, phase, cull, backfaceCull, chroma)
    fun addOuterStairF(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addOuterStairF(Color(color), x, y, z, type, left, lighting, phase, cull, backfaceCull, chroma)
    fun addOuterStairF(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        left: Boolean,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.StairOuterF,
                listOf(x + 0.5, y + 0.5, z + 0.5, (if (left) stairTypeTransform[type] else type).toDouble()),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma
            )
        )
    }

    fun addStairO(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(color, BlockPos(x, y, z), lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(color, BlockPos(x, y, z), lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(color, BlockPos(x, y, z), lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: Long,
        bp: BlockPos,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(color, bp.x, bp.y, bp.z, Minecraft.getMinecraft().theWorld.getBlockState(bp), lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: List<Double>,
        bp: BlockPos,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(color, bp.x, bp.y, bp.z, Minecraft.getMinecraft().theWorld.getBlockState(bp), lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: Color,
        bp: BlockPos,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(color, bp.x, bp.y, bp.z, Minecraft.getMinecraft().theWorld.getBlockState(bp), lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        bs: IBlockState,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(Color(color), x, y, z, RandomShit.comprehensiveStairMetadata(bs, BlockPos(x, y, z)), lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        bs: IBlockState,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(Color(color), x, y, z, RandomShit.comprehensiveStairMetadata(bs, BlockPos(x, y, z)), lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        bs: IBlockState,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(color, x, y, z, RandomShit.comprehensiveStairMetadata(bs, BlockPos(x, y, z)), lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(Color(color), x, y, z, type, lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) = addStairO(Color(color), x, y, z, type, lw, lighting, phase, smooth, cull, chroma)
    fun addStairO(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        smooth: Boolean,
        cull: Boolean,
        chroma: Int
    ) {
        if (type < 0) return
        if (type and 32 == 0) addStraightStairO(color, x, y, z, type and 7, lw, lighting, phase, smooth, cull, chroma)
        else if (type and 16 == 0) addInnerStairO(color, x, y, z, type and 7, type and 8 > 0, lw, lighting, phase, smooth, cull, chroma)
        else addOuterStairO(color, x, y, z, type and 7, type and 8 > 0, lw, lighting, phase, smooth, cull, chroma)
    }

    fun addStairF(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(color, BlockPos(x, y, z), lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(color, BlockPos(x, y, z), lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(color, BlockPos(x, y, z), lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: Long,
        bp: BlockPos,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(color, bp.x, bp.y, bp.z, Minecraft.getMinecraft().theWorld.getBlockState(bp), lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: List<Double>,
        bp: BlockPos,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(color, bp.x, bp.y, bp.z, Minecraft.getMinecraft().theWorld.getBlockState(bp), lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: Color,
        bp: BlockPos,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(color, bp.x, bp.y, bp.z, Minecraft.getMinecraft().theWorld.getBlockState(bp), lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        bs: IBlockState,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(Color(color), x, y, z, RandomShit.comprehensiveStairMetadata(bs, BlockPos(x, y, z)), lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        bs: IBlockState,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(Color(color), x, y, z, RandomShit.comprehensiveStairMetadata(bs, BlockPos(x, y, z)), lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        bs: IBlockState,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(color, x, y, z, RandomShit.comprehensiveStairMetadata(bs, BlockPos(x, y, z)), lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: Long,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(Color(color), x, y, z, type, lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: List<Double>,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addStairF(Color(color), x, y, z, type, lighting, phase, cull, backfaceCull, chroma)
    fun addStairF(
        color: Color,
        x: Int,
        y: Int,
        z: Int,
        type: Int,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) {
        if (type < 0) return
        if (type and 32 == 0) addStraightStairF(color, x, y, z, type and 7, lighting, phase, cull, backfaceCull, chroma)
        else if (type and 16 == 0) addInnerStairF(color, x, y, z, type and 7, type and 8 > 0, lighting, phase, cull, backfaceCull, chroma)
        else addOuterStairF(color, x, y, z, type and 7, type and 8 > 0, lighting, phase, cull, backfaceCull, chroma)
    }

    fun addBoxOJ(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        wx: Double,
        h: Double,
        wz: Double,
        centered: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBOJ(Color(color), if (centered) x - wx * 0.5 else x, y, if (centered) z - wz * 0.5 else z, if (centered) x + wx * 0.5 else x + wx, y + h, if (centered) z + wz * 0.5 else z + wz, lw, lighting, phase, cull, backfaceCull, chroma)
    fun addBoxOJ(
        color: List<Double>,
        x: Double,
        y: Double,
        z: Double,
        wx: Double,
        h: Double,
        wz: Double,
        centered: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBOJ(Color(color), if (centered) x - wx * 0.5 else x, y, if (centered) z - wz * 0.5 else z, if (centered) x + wx * 0.5 else x + wx, y + h, if (centered) z + wz * 0.5 else z + wz, lw, lighting, phase, cull, backfaceCull, chroma)
    fun addBoxOJ(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        wx: Double,
        h: Double,
        wz: Double,
        centered: Boolean,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBOJ(color, if (centered) x - wx * 0.5 else x, y, if (centered) z - wz * 0.5 else z, if (centered) x + wx * 0.5 else x + wx, y + h, if (centered) z + wz * 0.5 else z + wz, lw, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBOJM(
        color: Long,
        aabb: AxisAlignedBB,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBOJ(color, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lw, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBOJM(
        color: List<Double>,
        aabb: AxisAlignedBB,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBOJ(color, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lw, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBOJM(
        color: Color,
        aabb: AxisAlignedBB,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBOJ(color, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lw, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBOJ(
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
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBOJ(Color(color), x1, y1, z1, x2, y2, z2, lw, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBOJ(
        color: List<Double>,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        lw: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addAABBOJ(Color(color), x1, y1, z1, x2, y2, z2, lw, lighting, phase, cull, backfaceCull, chroma)
    fun addAABBOJ(
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
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.AABBOJ,
                listOf(x1, y1, z1, x2, y2, z2, lw),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma
            )
        )
    }

    fun addBillboard(
        color: Long,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addBillboard(Color(color), x, y, z, w, h, lighting, phase, cull, backfaceCull, chroma)
    fun addBillboard(
        color: List<Double>,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) = addBillboard(Color(color), x, y, z, w, h, lighting, phase, cull, backfaceCull, chroma)
    fun addBillboard(
        color: Color,
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        h: Double,
        lighting: Int,
        phase: Boolean,
        cull: Boolean,
        backfaceCull: Boolean,
        chroma: Int
    ) {
        addThing(
            Thingamabob(
                Thingamabob.Type.Billboard,
                listOf(x, y, z, w, h),
                color,
                1f,
                lighting,
                phase,
                false,
                cull,
                backfaceCull,
                chroma
            )
        )
    }

    @JvmField
    var USE_NEW_SHIT: Boolean = false
    @JvmField
    var CAN_USE_CHROMA: Boolean = false
    private var checked = false
    private var errored = false
    fun render(pt: Double, t: Int) {
        if (!checked) {
            val cap = GLContext.getCapabilities()
            USE_NEW_SHIT = cap.GL_NV_primitive_restart && cap.OpenGL15
            CAN_USE_CHROMA = cap.OpenGL20
            checked = true
            // glEnable(GL43.GL_DEBUG_OUTPUT)
            // GL43.glDebugMessageCallback(KHRDebugCallback { source: Int, type: Int, id: Int, severity: Int, message: String? ->
            //     if (severity == GL43.GL_DEBUG_SEVERITY_NOTIFICATION) return@KHRDebugCallback
            //     println("ye fucked up")
            //     println("source: $source type: $type id: $id severity $severity message: $message")
            // })
        }

        Geometry.cacheValues()

        val prof = Minecraft.getMinecraft().mcProfiler
        prof.startSection("Apelles")
        GlState.reset()
        GlState.push()

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
            glEnableClientState(GL_VERTEX_ARRAY)
        }
        if (CAN_USE_CHROMA) ChromaShader.updateUniforms(pt, t)

        glDisable(GL_BLEND)
        glDepthMask(true)

        glEnable(GL_TEXTURE_2D)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT.toFloat())

        prof.startSection("outlines")
        EntityOutlineRenderer.checkEntities(pt)

        prof.endStartSection("texturedOpaque")
        texturedOpaque.sort()
        if (USE_NEW_SHIT) {
            prof.startSection("prepare")
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
            prof.endSection()
        }
        prof.startSection("render")
        texturedOpaque.forEach {
            if (USE_NEW_SHIT) Geometry.bind(it)
            it.render(pt)
        }
        prof.endStartSection("postRender")
        texturedOpaque.clear()
        if (USE_NEW_SHIT) Geometry.render(pt)
        prof.endSection()

        glEnable(GL_BLEND)
        OpenGlHelper.glBlendFunc(770, 771, 1, 771)
        glDepthMask(false)

        prof.endStartSection("texturedTranslucent")
        texturedTranslucent.sort()
        if (USE_NEW_SHIT) {
            prof.startSection("prepare")
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
            prof.endSection()
        }
        prof.startSection("render")
        texturedTranslucent.forEach {
            if (USE_NEW_SHIT) Geometry.bind(it)
            it.render(pt)
        }
        prof.endStartSection("postRender")
        texturedTranslucent.clear()
        if (USE_NEW_SHIT) Geometry.render(pt)
        prof.endSection()
        glDisable(GL_TEXTURE_2D)

        glDisable(GL_BLEND)
        glDepthMask(true)

        prof.endStartSection("opaque")
        opaque.sort()
        if (USE_NEW_SHIT) {
            prof.startSection("prepare")
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
            prof.endSection()
        }
        prof.startSection("render")
        opaque.forEach {
            if (USE_NEW_SHIT) Geometry.bind(it)
            it.render(pt)
        }
        prof.endStartSection("postRender")
        opaque.clear()
        if (USE_NEW_SHIT) Geometry.render(pt)
        prof.endSection()

        glEnable(GL_BLEND)
        // OpenGlHelper.glBlendFunc(770, 771, 1, 771)
        glDepthMask(false)

        prof.endStartSection("translucent")
        translucent.sort()
        if (USE_NEW_SHIT) {
            prof.startSection("prepare")
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
            prof.endSection()
        }
        prof.startSection("render")
        translucent.forEach {
            if (USE_NEW_SHIT) Geometry.bind(it)
            it.render(pt)
        }
        prof.endStartSection("postRender")
        translucent.clear()
        if (USE_NEW_SHIT) {
            Geometry.render(pt)

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
            glDisable(GL31.GL_PRIMITIVE_RESTART)
            glDisableClientState(GL_VERTEX_ARRAY)
            GlState.setColorArray(false)
            GlState.setNormalArray(false)
            GlState.setTexArray(false)
        }
        prof.endSection()
        if (CAN_USE_CHROMA) GlState.bindShader(0)

        if (!errored) {
            try {
                prof.endStartSection("outlines")
                EntityOutlineRenderer.renderOutlines(pt)
            } catch (e: Exception) {
                println("gg shitter")
                e.printStackTrace()
                errored = true
            } finally {
                prof.endSection()
            }
        }
        GlState.pop()
        prof.endSection()
    }
}