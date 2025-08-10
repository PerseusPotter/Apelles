package com.perseuspotter.apelles.geo.dim3.octahedron

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object OctahedronOutline : Geometry() {
    override val name = "octahedronO"
    override fun render(pt: Double) {
        val (_x, _y, _z, _w, _h) = currentParams
        val (x, y, z, s) = rescale(_x, _y, _z)
        val w = _w * s
        val h = _h * s

        begin(GL11.GL_LINES, false)

        addVert(x, y + h, z) // 0
        addVert(x + w, y, z - w) // 1
        addVert(x - w, y, z - w) // 2
        addVert(x - w, y, z + w) // 3
        addVert(x + w, y, z + w) // 4
        addVert(x, y - h, z) // 5

        emitVert(0); emitVert(1)
        emitVert(1); emitVert(2)
        emitVert(2); emitVert(0)
        emitVert(0); emitVert(3)
        emitVert(3); emitVert(4)
        emitVert(4); emitVert(0)
        emitVert(5); emitVert(2)
        emitVert(2); emitVert(3)
        emitVert(3); emitVert(5)
        emitVert(5); emitVert(4)
        emitVert(4); emitVert(1)
        emitVert(1); emitVert(5)

        draw()
    }

    override fun inView(): Boolean {
        val (x, y, z, w, h) = currentParams
        return false ||
            Frustum.test(x, y + h, z) ||
            Frustum.test(x + w, y, z + w) ||
            Frustum.test(x + w, y, z - w) ||
            Frustum.test(x - w, y, z + w) ||
            Frustum.test(x - w, y, z - w) ||
            Frustum.test(x, y - h, z)
    }

    override fun getVertexCount(): Int = 6
    override fun getIndexCount(): Int = 12 * 2
    override fun getDrawMode(): Int = GL11.GL_LINES
}