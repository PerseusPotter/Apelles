package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.state.Color
import org.lwjgl.opengl.GL11

object PrimitiveInternal : Geometry() {
    override val name = "primitiveinternal"
    override fun render(pt: Double, params: List<Double>) {
        begin(GL11.GL_TRIANGLES, false, params[0], params[1], params[2])
        val iter = params.iterator()
        while (iter.hasNext()) {
            val r = iter.next()
            val g = iter.next()
            val b = iter.next()
            val a = iter.next()
            if (Renderer.USE_NEW_SHIT) currCol = Color(r, g, b, a)
            for (i in 0 until 6) pos(iter.next(), iter.next(), iter.next())
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: List<Double>): Int = params.size / 22 * 6
    override fun getIndicesCount(params: List<Double>): Int = params.size / 22 * 6
    override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLES
}