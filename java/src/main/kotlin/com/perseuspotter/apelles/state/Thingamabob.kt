package com.perseuspotter.apelles.state

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.Geometry3D
import net.minecraft.util.ResourceLocation

open class Thingamabob(
    val type: Type,
    val params: DoubleArray,
    val color: Color,
    val lw: Float,
    val lighting: Int,
    val phase: Boolean,
    val smooth: Boolean,
    val cull: Boolean,
    val tex: ResourceLocation? = null
) : Comparable<Thingamabob> {
    fun prerender() {
        GlState.lineWidth(lw)
        GlState.setLighting(lighting)
        if (!Renderer.USE_NEW_SHIT) GlState.color(
            color.r,
            color.g,
            color.b,
            color.a
        )
        GlState.setDepthTest(!phase)
        GlState.lineSmooth(smooth)
        if (tex != null) GlState.bindTexture(tex)
    }

    fun getRenderer(): Geometry = when (type) {
        Type.Line -> Geometry3D.line
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
    }

    open fun render(pt: Double) {
        if (color.a == 0f) return

        val geo = getRenderer()

        if (cull) {
            val points = geo.testPoints(params)
            if (points.isNotEmpty() && points.all { !Frustum.test(it) }) return
        }

        if (!Renderer.USE_NEW_SHIT) prerender()

        geo.render(pt, params)
    }

    open fun getVertexCount() = getRenderer().getVertexCount(params)
    open fun getIndicesCount() = getRenderer().getIndicesCount(params)
    open fun getDrawMode() = getRenderer().getDrawMode()

    // who cares about sorting translucent objects by distance?
    fun getRenderPriority(): Int {
        return (if (lighting > 0) 4 else 0) or
                (if (phase) 2 else 0) or
                (if (smooth) 1 else 0)
    }

    // praying for no collisions
    fun getVBOGroupingId(): Int {
        return getRenderPriority() xor
                lw.toRawBits() xor
                (if (lighting == 1) 8 else 0) xor
                (if (lighting == 2) 16 else 0) xor
                (getDrawMode() shl 6) xor
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

    enum class Type() {
        Line,
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

        BeaconI,
        BeaconO
    }
}