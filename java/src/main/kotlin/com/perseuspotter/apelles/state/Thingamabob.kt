package com.perseuspotter.apelles.state

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.depression.ChromaShader
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.Geometry3D
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
                val shader = (getRenderer().shader ?: ChromaShader.normalBundle).get(chroma, tex != null)
                shader.bind()
            } else GlState.bindShader(0)
        }
        GlState.setDepthTest(!phase)
        GlState.lineSmooth(smooth)
        if (tex != null) GlState.bindTexture(tex)
    }

    fun getRenderer(): Geometry = when (type) {
        Type.Primitive -> Geometry3D.primitive
        Type.AABBO -> Geometry3D.aabbO
        Type.AABBF -> Geometry3D.aabbF
        Type.BeaconI -> Geometry3D.beaconI
        Type.BeaconO -> Geometry3D.beaconO
        Type.BeaconTI -> Geometry3D.beaconTI
        Type.BeaconTO -> Geometry3D.beaconTO
        Type.Icosphere -> Geometry3D.icosphere
        Type.PyramidO -> Geometry3D.pyramidO
        Type.PyramidF -> Geometry3D.pyramidF
        Type.VertCylinderR -> Geometry3D.vertCylinderR
        Type.VertCylinderC -> Geometry3D.vertCylinderC
        Type.OctahedronO -> Geometry3D.octahedronO
        Type.OctahedronF -> Geometry3D.octahedronF
        Type.StairStraightO -> Geometry3D.stairStraightO
        Type.StairStraightF -> Geometry3D.stairStraightF
        Type.StairInnerO -> Geometry3D.stairInnerO
        Type.StairInnerF -> Geometry3D.stairInnerF
        Type.StairOuterO -> Geometry3D.stairOuterO
        Type.StairOuterF -> Geometry3D.stairOuterF
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
        return (if (lighting > 0) 8 else 0) or
                (if (chroma > 0) 4 else 0) or
                (if (phase) 2 else 0) or
                (if (smooth) 1 else 0)
    }

    // praying for no collisions
    fun getVBOGroupingId(): Int {
        return getRenderPriority() xor
                lw.toRawBits() xor
                (if (lighting == 1) 16 else 0) xor
                (if (lighting == 2) 32 else 0) xor
                (if (chroma == 1) 64 else 0) xor
                (if (chroma == 2) 128 else 0) xor
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

        BeaconI,
        BeaconO
    }
}