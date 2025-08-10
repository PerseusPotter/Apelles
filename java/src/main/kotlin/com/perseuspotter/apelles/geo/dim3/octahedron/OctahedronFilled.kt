package com.perseuspotter.apelles.geo.dim3.octahedron

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object OctahedronFilled : Geometry() {
    override val name = "octahedronF"
    override fun render(pt: Double) {
        val (_x, _y, _z, _w, _h) = currentParams
        val (x, y, z, s) = rescale(_x, _y, _z)
        val w = _w * s
        val h = _h * s

        begin(GL11.GL_TRIANGLES, false)

        addVert(x, y + h, z) // 0
        addVert(x + w, y, z - w) // 1
        addVert(x - w, y, z - w) // 2
        addVert(x - w, y, z + w) // 3
        addVert(x + w, y, z + w) // 4
        addVert(x, y - h, z) // 5

        addTri(0, 1, 2)
        addTri(2, 1, 5)
        addTri(2, 5, 3)
        addTri(3, 5, 4)
        addTri(3, 4, 0)
        addTri(0, 4, 1)

        addTri(1, 4, 5)
        addTri(0, 2, 3)

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

    override fun getVertexCount(): Int = 8
    override fun getIndexCount(): Int = 8 * 3
    override fun getDrawMode(): Int = GL11.GL_TRIANGLES
}