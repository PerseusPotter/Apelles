package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.component6
import org.lwjgl.opengl.GL11
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object VerticalCylinderFilled : Geometry() {
    override val name = "vertCylinderF"
    override fun render(pt: Double) {
        val (_x, _y, _z, _r, _h, _n) = currentParams
        val (x, y, z, s) = rescale(_x, _y, _z)
        val r = _r * s
        val h = _h * s
        val n = _n.toInt()

        begin(GL11.GL_TRIANGLES, false)
        addVert(x, y, z)
        addVert(x, y + h, z)
        addVert(x + r, y, z)
        addVert(x + r, y + h, z)
        for (i in 1 until n) {
            val a = 2.0 * PI * i / n
            val rx = cos(a) * r
            val rz = sin(a) * r
            addVert(
                x + rx,
                y,
                z + rz
            )
            addVert(
                x + rx,
                y + h,
                z + rz
            )
        }

        addTri(0, 2 * n, 2)
        addTri(1, 3, 2 * n + 1)
        addTri(2 * n, 2 * n + 1, 2)
        addTri(2, 2 * n + 1, 3)
        for (i in 1 until n) {
            addTri(0, 2 * i, 2 * i + 2)
            addTri(1, 2 * i + 3, 2 * i + 1)
            addTri(2 * i, 2 * i + 1, 2 * i + 2)
            addTri(2 * i + 2, 2 * i + 1, 2 * i + 3)
        }

        draw()
    }

    override fun inView(): Boolean {
        val (x, y, z, r, h) = currentParams
        return false ||
            Frustum.test(x, y + h, z) ||
            Frustum.test(x + r, y, z + r) ||
            Frustum.test(x + r, y, z - r) ||
            Frustum.test(x - r, y, z + r) ||
            Frustum.test(x - r, y, z - r)
    }

    override fun getVertexCount(): Int = 2 * (currentParams[5].toInt() + 1)
    override fun getIndexCount(): Int = 4 * currentParams[5].toInt() * 3
    override fun getDrawMode(): Int = GL11.GL_TRIANGLES
}