package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.GeometryInternal
import com.perseuspotter.apelles.state.Color
import org.lwjgl.opengl.GL11

object PrimitiveColorUVInternal : GeometryInternal() {
    override val name = "primitivecoloruvinternal"
    override fun render(pt: Double, params: DoubleArray, N: Int) {
        begin(GL11.GL_TRIANGLES, true, params[0], params[1], params[2])
        var i = 0
        while (i < N) {
            val r = params[i++]
            val g = params[i++]
            val b = params[i++]
            val a = params[i++]
            if (Renderer.USE_NEW_SHIT) currCol = Color(r, g, b, a)
            for (k in 0 until 6) pos(params[i++], params[i++], params[i++], params[i++], params[i++])
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: DoubleArray, N: Int): Int = N / 34 * 6
    override fun getIndicesCount(params: DoubleArray, N: Int): Int = N / 34 * 6
    override fun getDrawMode(params: DoubleArray, N: Int): Int = GL11.GL_TRIANGLES
}