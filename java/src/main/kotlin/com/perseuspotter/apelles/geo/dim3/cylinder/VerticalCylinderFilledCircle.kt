package com.perseuspotter.apelles.geo.dim3.cylinder

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.component6
import org.lwjgl.opengl.GL11
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object VerticalCylinderFilledCircle : Geometry() {
    override val name = "vertCylinderC"
    override fun render(pt: Double, params: List<Double>) {
        val (_x, _y, _z, _r, _h, _n) = params
        val (x, y, z, s) = rescale(_x, _y, _z)
        val r = _r * s
        val h = _h * s
        val n = _n.toInt()

        if (Renderer.USE_NEW_SHIT) {
            begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 2.0, z)
            addVert(x, y, z)
            addVert(x + r, y, z)
            for (i in 1 until n) {
                val a = 2.0 * PI * i / n
                addVert(
                    x + cos(a) * r,
                    y,
                    z + sin(a) * r
                )
            }
            addVert(x, y + h, z)
            addVert(x + r, y + h, z)
            for (i in 1 until n) {
                val a = 2.0 * PI * i / n
                addVert(
                    x + cos(a) * r,
                    y + h,
                    z + sin(a) * r
                )
            }

            index(0)
            for (i in 1..n) index(i)
            index(1)
            reset()
            index(n + 1)
            index(n + 2)
            for (i in 2 * n + 1 downTo n + 2) index(i)
            draw()
        } else {
            begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 2.0, z)
            pos(x, y + h, z)

            pos(
                x + r,
                y + h,
                z
            )
            for (i in 1 until n) {
                val a = 2.0 * PI * (n - i) / n
                pos(
                    x + cos(a) * r,
                    y + h,
                    z + sin(a) * r
                )
            }
            pos(
                x + r,
                y + h,
                z
            )
            draw()

            begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 2.0, z)
            pos(x, y, z)

            pos(
                x + r,
                y,
                z
            )
            for (i in 1 until n) {
                val a = 2.0 * PI * i / n
                pos(
                    x + cos(a) * r,
                    y,
                    z + sin(a) * r
                )
            }
            pos(
                x + r,
                y,
                z
            )
            draw()
        }
    }

    override fun inView(params: List<Double>): Boolean {
        val (x, y, z, r, h) = params
        return false ||
                Frustum.test(x, y + h, z) ||
                Frustum.test(x + r, y, z + r) ||
                Frustum.test(x + r, y, z - r) ||
                Frustum.test(x - r, y, z + r) ||
                Frustum.test(x - r, y, z - r)
    }

    override fun getVertexCount(params: List<Double>): Int = 2 * (params[5].toInt() + 1)
    override fun getIndicesCount(params: List<Double>): Int = 2 * (params[5].toInt() + 2) + if (Renderer.USE_NEW_SHIT) 1 else 0
    override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_FAN
}