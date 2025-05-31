package com.perseuspotter.apelles.geo.dim3.octahedron

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object OctahedronOutline : Geometry() {
    override val name = "octahedronO"
    override fun render(pt: Double, params: List<Double>) {
        val (_x, _y, _z, _w, _h) = params
        val (x, y, z, s) = rescale(_x, _y, _z)
        val w = _w * s
        val h = _h * s

        if (Renderer.USE_NEW_SHIT) {
            begin(GL11.GL_LINE_STRIP, false, x, y, z)
            addVert(x, y + h, z)
            addVert(x + w, y, z - w)
            addVert(x - w, y, z - w)
            addVert(x - w, y, z + w)
            addVert(x + w, y, z + w)
            addVert(x, y - h, z)

            index(0)
            index(1)
            index(2)
            index(0)
            index(3)
            index(4)
            index(0)
            reset()
            index(5)
            index(2)
            index(3)
            index(5)
            index(4)
            index(1)
            index(5)
        } else {
            begin(GL11.GL_LINES, false, x, y, z)
            pos(x, y + h, z); pos(x + w, y, z + w)
            pos(x, y + h, z); pos(x + w, y, z - w)
            pos(x, y + h, z); pos(x - w, y, z + w)
            pos(x, y + h, z); pos(x - w, y, z - w)

            pos(x + w, y, z + w); pos(x + w, y, z - w)
            pos(x + w, y, z - w); pos(x - w, y, z - w)
            pos(x - w, y, z - w); pos(x - w, y, z + w)
            pos(x - w, y, z + w); pos(x + w, y, z + w)

            pos(x, y - h, z); pos(x + w, y, z + w)
            pos(x, y - h, z); pos(x + w, y, z - w)
            pos(x, y - h, z); pos(x - w, y, z + w)
            pos(x, y - h, z); pos(x - w, y, z - w)
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean {
        val (x, y, z, w, h) = params
        return false ||
                Frustum.test(x, y + h, z) ||
                Frustum.test(x + w, y, z + w) ||
                Frustum.test(x + w, y, z - w) ||
                Frustum.test(x - w, y, z + w) ||
                Frustum.test(x - w, y, z - w) ||
                Frustum.test(x, y - h, z)
    }

    override fun getVertexCount(params: List<Double>): Int = 6
    override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 15 else 24
    override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_LINE_STRIP else GL11.GL_LINES
}