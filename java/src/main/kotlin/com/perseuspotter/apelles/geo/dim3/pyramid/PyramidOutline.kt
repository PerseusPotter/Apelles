package com.perseuspotter.apelles.geo.dim3.pyramid

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.component6
import org.lwjgl.opengl.GL11
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object PyramidOutline : Geometry() {
    override val name = "pyramidO"
    override fun render(pt: Double, params: List<Double>) {
        val (_x, _y, _z, _r, _h, _n) = params
        val (x, y, z, s) = rescale(_x, _y, _z)
        val r = _r * s
        val h = _h * s
        val n = _n.toInt()

        val a0 = if (n == 4) PI / 4.0 else 0.0
        if (Renderer.USE_NEW_SHIT) {
            begin(GL11.GL_LINE_STRIP, false, x, y + h / 4.0, z)
            addVert(x, y + h, z)
            for (i in 0 until n) {
                val a = a0 + 2.0 * PI * i / n
                addVert(
                    x + cos(a) * r,
                    y,
                    z + sin(a) * r
                )
            }

            for (i in 1..n) index(i)
            index(1)
            for (i in 1 until n step 2) {
                reset()
                index(i)
                index(0)
                index(i + 1)
            }
            if (n and 1 == 1) {
                reset()
                index(n)
                index(0)
            }
        } else {
            begin(GL11.GL_LINES, false, x, y + h / 4.0, z)

            for (i in 0 until n) {
                val a = a0 + 2.0 * PI * i / n
                if (i > 0) pos(
                    x + cos(a) * r,
                    y,
                    z + sin(a) * r
                )
                pos(
                    x + cos(a) * r,
                    y,
                    z + sin(a) * r
                )
                pos(x, y + h, z)
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
        }
        draw()
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

    override fun getVertexCount(params: List<Double>): Int = 1 + params[5].toInt()
    override fun getIndicesCount(params: List<Double>): Int {
        val n = params[5].toInt()
        return if (Renderer.USE_NEW_SHIT) 3 * n + 1 + (n and 1) else 4 * n
    }
    override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_LINE_STRIP else GL11.GL_LINES
}