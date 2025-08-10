package com.perseuspotter.apelles.geo.dim3.pyramid

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.component6
import org.lwjgl.opengl.GL11
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object PyramidOutline : Geometry() {
    override val name = "pyramidO"
    override fun render(pt: Double) {
        val (_x, _y, _z, _r, _h, _n) = currentParams
        val (x, y, z, s) = rescale(_x, _y, _z)
        val r = _r * s
        val h = _h * s
        val n = _n.toInt()

        val a0 = if (n == 4) PI / 4.0 else 0.0

        begin(GL11.GL_LINES, false)
        addVert(x, y + h, z)
        for (i in 0 until n) {
            val a = a0 + 2.0 * PI * i / n
            addVert(
                x + cos(a) * r,
                y,
                z + sin(a) * r
            )
        }

        emitVert(n); emitVert(1)
        emitVert(1); emitVert(0)
        for (i in 2..n) {
            emitVert(i - 1); emitVert(i)
            emitVert(i); emitVert(0)
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

    override fun getVertexCount(): Int = 1 + currentParams[5].toInt()
    override fun getIndexCount(): Int = 2 * currentParams[5].toInt() * 2
    override fun getDrawMode(): Int = GL11.GL_LINES
}