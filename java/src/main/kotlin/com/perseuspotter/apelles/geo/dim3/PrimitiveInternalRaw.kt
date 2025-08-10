package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.geo.GeometryInternal
import org.lwjgl.opengl.GL11.GL_TRIANGLES

object PrimitiveInternalRaw : GeometryInternal() {
    override val name = "primitiveinternalraw"
    override fun render(pt: Double, params: DoubleArray, N: Int) {
        val mode = params[0].toInt()
        begin(mode, false)

        if (mode == GL_TRIANGLES) {
            var i = 1
            while (i < N) {
                addTri(
                    addVertRaw(params[i++], params[i++], params[i++]),
                    addVertRaw(params[i++], params[i++], params[i++]),
                    addVertRaw(params[i++], params[i++], params[i++])
                )
            }
        } else {
            var i = 1
            while (i < N) {
                posRaw(params[i++], params[i++], params[i++], 0.0, 1.0, 0.0)
            }
        }

        draw()
    }

    override fun inView(params: DoubleArray, N: Int): Boolean = true

    override fun getVertexCount(params: DoubleArray, N: Int): Int = (N - 1) / 3
    override fun getIndexCount(params: DoubleArray, N: Int): Int = (N - 1) / 3
    override fun getDrawMode(params: DoubleArray, N: Int): Int = params[0].toInt()
}