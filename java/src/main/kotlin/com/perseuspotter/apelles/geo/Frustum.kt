package com.perseuspotter.apelles.geo

import net.minecraft.client.renderer.culling.ClippingHelperImpl
import net.minecraft.util.AxisAlignedBB

object Frustum {
    private val implInst: ClippingHelperImpl
    init {
        val implInstF = ClippingHelperImpl::class.java.getDeclaredField("field_78563_e")
        implInstF.isAccessible = true
        implInst = (implInstF.get(null) as ClippingHelperImpl)
    }
    private fun getFrustum() = implInst.frustum

    // this never tests the far plane ([4]) because we always rescale to be within

    fun test(x: Double, y: Double, z: Double): Boolean {
        val xt = x - Geometry.getRenderX()
        val yt = y - Geometry.getRenderY()
        val zt = z - Geometry.getRenderZ()
        val f = getFrustum()
        for (i in 0..5) {
            if (i == 4) continue
            val p = f[i]
            if (p[0] * xt + p[1] * yt + p[2] * zt + p[3] < 0.0) return false
        }
        return true
    }
    fun test(p: Point): Boolean = test(p.x, p.y, p.z)

    fun checkAABB(aabb: AxisAlignedBB): Boolean =
        test(aabb.minX, aabb.minY, aabb.minZ) ||
        test(aabb.minX, aabb.minY, aabb.maxZ) ||
        test(aabb.minX, aabb.maxY, aabb.minZ) ||
        test(aabb.minX, aabb.maxY, aabb.maxZ) ||
        test(aabb.maxX, aabb.minY, aabb.minZ) ||
        test(aabb.maxX, aabb.minY, aabb.maxZ) ||
        test(aabb.maxX, aabb.maxY, aabb.minZ) ||
        test(aabb.maxX, aabb.maxY, aabb.maxZ)

    fun clip(p1: Point, p2: Point): Array<Point?> {
        val rx = Geometry.getRenderX()
        val ry = Geometry.getRenderY()
        val rz = Geometry.getRenderZ()
        var x1 = p1.x - rx
        var y1 = p1.y - ry
        var z1 = p1.z - rz
        var x2 = p2.x - rx
        var y2 = p2.y - ry
        var z2 = p2.z - rz
        val f = getFrustum()
        for (i in 0..5) {
            if (i == 4) continue
            val p = f[i]
            val m = p[0] * x1 + p[1] * y1 + p[2] * z1 + p[3]
            val n = p[0] * x2 + p[1] * y2 + p[2] * z2 + p[3]
            if (m > 0.0 && n > 0.0) continue
            if (m < 0.0 && n < 0.0) return arrayOf(null, null)
            val a = p[0] * x1 + p[1] * y1 + p[2] * z1 + p[3]
            val b = p[0] * (x1 - x2) + p[1] * (y1 - y2) + p[2] * (z1 - z2)
            val u = if (b == 0.0) 0.0 else a / b
            if (m < 0.0) {
                x1 = x1 + u * (x2 - x1)
                y1 = y1 + u * (y2 - y1)
                z1 = z1 + u * (z2 - z1)
            } else {
                x2 = x1 + u * (x2 - x1)
                y2 = y1 + u * (y2 - y1)
                z2 = z1 + u * (z2 - z1)
            }
        }
        return arrayOf(
            Point(x1 + rx, y1 + ry, z1 + rz),
            Point(x2 + rx, y2 + ry, z2 + rz)
        )
    }
}