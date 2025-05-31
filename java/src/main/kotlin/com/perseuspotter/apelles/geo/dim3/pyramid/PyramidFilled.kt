package com.perseuspotter.apelles.geo.dim3.pyramid

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.component6
import org.lwjgl.opengl.GL11
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object PyramidFilled : Geometry() {
    override val name = "pyramidF"
    override fun render(pt: Double, params: List<Double>) {
        val (_x, _y, _z, _r, _h, _n) = params
        val (x, y, z, s) = rescale(_x, _y, _z)
        val r = _r * s
        val h = _h * s
        val n = _n.toInt()

        val a0 = if (n == 4) PI / 4.0 else 0.0

        if (Renderer.USE_NEW_SHIT) {
            begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 4.0, z)
            addVert(x, y, z)
            addVert(x, y + h, z)
            for (i in 0 until n) {
                val a = a0 + 2.0 * PI * (if (h > 0) n - i else i) / n
                addVert(
                    x + cos(a) * r,
                    y,
                    z + sin(a) * r
                )
            }

            index(1)
            for (i in 2..n + 1) index(i)
            index(2)
            reset()
            index(0)
            for (i in n + 1 downTo 2) index(i)
            index(n + 1)
            draw()
        } else {
            begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 4.0, z)
            pos(x, y + h, z)

            for (i in 0 until n) {
                val a = a0 + 2.0 * PI * (if (h > 0) n - i else i) / n
                pos(
                    x + cos(a) * r,
                    y,
                    z + sin(a) * r
                )
            }
            pos(
                x + cos(a0) * r,
                y,
                z + sin(a0) * r
            )
            draw()

            begin(GL11.GL_TRIANGLE_FAN, false, x, y + h / 4.0, z)
            pos(x, y, z)

            for (i in 0 until n) {
                val a = a0 + 2.0 * PI * (if (h > 0) i else n - i) / n
                pos(
                    x + cos(a) * r,
                    y,
                    z + sin(a) * r
                )
            }
            pos(
                x + cos(a0) * r,
                y,
                z + sin(a0) * r
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

    override fun getVertexCount(params: List<Double>): Int = 2 + params[5].toInt()
    override fun getIndicesCount(params: List<Double>): Int = 2 * (params[5].toInt() + 2) + if (Renderer.USE_NEW_SHIT) 1 else 0
    override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_FAN
}