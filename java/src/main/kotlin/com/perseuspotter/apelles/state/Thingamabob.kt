package com.perseuspotter.apelles.state

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.depression.ChromaShader
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.dim3.Billboard
import com.perseuspotter.apelles.geo.dim3.Icosphere
import com.perseuspotter.apelles.geo.dim3.Primitive
import com.perseuspotter.apelles.geo.dim3.aabb.AABBFilled
import com.perseuspotter.apelles.geo.dim3.aabb.AABBOutline
import com.perseuspotter.apelles.geo.dim3.aabb.AABBOutlineJoined
import com.perseuspotter.apelles.geo.dim3.beacon.BeaconInside
import com.perseuspotter.apelles.geo.dim3.beacon.BeaconOutside
import com.perseuspotter.apelles.geo.dim3.beacon.BeaconTopInside
import com.perseuspotter.apelles.geo.dim3.beacon.BeaconTopOutside
import com.perseuspotter.apelles.geo.dim3.cylinder.VerticalCylinderFilledCircle
import com.perseuspotter.apelles.geo.dim3.cylinder.VerticalCylinderFilledRectangle
import com.perseuspotter.apelles.geo.dim3.octahedron.OctahedronFilled
import com.perseuspotter.apelles.geo.dim3.octahedron.OctahedronOutline
import com.perseuspotter.apelles.geo.dim3.pyramid.PyramidFilled
import com.perseuspotter.apelles.geo.dim3.pyramid.PyramidOutline
import com.perseuspotter.apelles.geo.dim3.stair.inner.StairInnerFilled
import com.perseuspotter.apelles.geo.dim3.stair.inner.StairInnerOutline
import com.perseuspotter.apelles.geo.dim3.stair.outer.StairOuterFilled
import com.perseuspotter.apelles.geo.dim3.stair.outer.StairOuterOutline
import com.perseuspotter.apelles.geo.dim3.stair.straight.StairStraightFilled
import com.perseuspotter.apelles.geo.dim3.stair.straight.StairStraightOutline
import net.minecraft.util.ResourceLocation

open class Thingamabob(
    val type: Type,
    val params: List<Double>,
    val color: Color,
    val lw: Float,
    val lighting: Int,
    val phase: Boolean,
    val smooth: Boolean,
    val cull: Boolean,
    val backfaceCull: Boolean,
    val chroma: Int,
    val tex: ResourceLocation? = null
) : Comparable<Thingamabob> {
    fun prerender(pt: Double) {
        GlState.lineWidth(lw)
        GlState.setLighting(lighting)
        if (!Renderer.USE_NEW_SHIT) {
            GlState.color(
                color.r,
                color.g,
                color.b,
                color.a
            )
        }
        if (Renderer.CAN_USE_CHROMA) {
            if (chroma > 0) {
                val shader = ChromaShader.get(chroma == 1, tex != null)
                shader.bind()
            } else GlState.bindShader(0)
        }
        GlState.setDepthTest(!phase)
        GlState.lineSmooth(smooth)
        if (tex != null) GlState.bindTexture(tex)
        GlState.setBackfaceCull(backfaceCull)
    }

    fun getRenderer(): Geometry = when (type) {
        Type.Primitive -> Primitive
        Type.AABBO -> AABBOutline
        Type.AABBF -> AABBFilled
        Type.BeaconI -> BeaconInside
        Type.BeaconO -> BeaconOutside
        Type.BeaconTI -> BeaconTopInside
        Type.BeaconTO -> BeaconTopOutside
        Type.Icosphere -> Icosphere
        Type.PyramidO -> PyramidOutline
        Type.PyramidF -> PyramidFilled
        Type.VertCylinderR -> VerticalCylinderFilledRectangle
        Type.VertCylinderC -> VerticalCylinderFilledCircle
        Type.OctahedronO -> OctahedronOutline
        Type.OctahedronF -> OctahedronFilled
        Type.StairStraightO -> StairStraightOutline
        Type.StairStraightF -> StairStraightFilled
        Type.StairInnerO -> StairInnerOutline
        Type.StairInnerF -> StairInnerFilled
        Type.StairOuterO -> StairOuterOutline
        Type.StairOuterF -> StairOuterFilled
        Type.AABBOJ -> AABBOutlineJoined
        Type.Billboard -> Billboard
    }

    open fun render(pt: Double) {
        if (color.a == 0f) return

        val geo = getRenderer()

        if (cull && !geo.inView(params)) return

        if (!Renderer.USE_NEW_SHIT) prerender(pt)

        geo.render(pt, params)
    }

    open fun getVertexCount() = getRenderer().getVertexCount(params)
    open fun getIndicesCount() = getRenderer().getIndicesCount(params)
    open fun getDrawMode() = getRenderer().getDrawMode(params)

    // who cares about sorting translucent objects by distance?
    fun getRenderPriority(): Int {
        return (if (lighting > 0) 16 else 0) or
                (if (chroma > 0) 8 else 0) or
                (if (phase) 4 else 0) or
                (if (smooth) 2 else 0) or
                (if (backfaceCull) 1 else 0)
    }

    // praying for no collisions
    fun getVBOGroupingId(): Int {
        return getRenderPriority() xor
                lw.toRawBits() xor
                (if (lighting == 1) 32 else 0) xor
                (if (lighting == 2) 64 else 0) xor
                (if (chroma == 1) 128 else 0) xor
                (if (chroma == 2) 256 else 0) xor
                (getDrawMode() shl 8) xor
                // color.r.toRawBits() xor
                // color.g.toRawBits() xor
                // color.b.toRawBits() xor
                // color.a.toRawBits() xor
                (tex?.hashCode() ?: 0)
    }

    companion object {
        private var idC = 0
    }
    private val id = idC++
    override fun compareTo(other: Thingamabob): Int {
        if (other.getRenderPriority() != getRenderPriority()) return other.getRenderPriority() - getRenderPriority()
        return id - other.id
    }

    enum class Type {
        Primitive,
        AABBO,
        AABBF,
        BeaconTI,
        BeaconTO,
        Icosphere,
        PyramidO,
        PyramidF,
        VertCylinderR,
        VertCylinderC,
        OctahedronO,
        OctahedronF,
        StairStraightO,
        StairStraightF,
        StairInnerO,
        StairInnerF,
        StairOuterO,
        StairOuterF,
        AABBOJ,
        Billboard,

        BeaconI,
        BeaconO
    }
}