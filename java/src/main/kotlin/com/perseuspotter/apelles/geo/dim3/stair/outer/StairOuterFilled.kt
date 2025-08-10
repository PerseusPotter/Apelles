package com.perseuspotter.apelles.geo.dim3.stair.outer

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object StairOuterFilled : Geometry() {
    override val name: String = "stairOuterF"
    override fun render(pt: Double) {
        val (_x, _y, _z, _c) = currentParams
        val (x, y, z, s) = rescale(_x, _y, _z)
        val verts = OrientationOuter.vertices[_c.toInt()]

        begin(GL11.GL_TRIANGLES, false)

        verts.forEach { addVert(x + it.x * s, y + it.y * s, z + it.z * s) }

        addTri(13, 11, 3)
        addTri(3, 11, 7)
        addTri(3, 7, 1)
        addTri(1, 7, 5)
        addTri(1, 5, 0)
        addTri(0, 5, 4)
        addTri(0, 4, 2)
        addTri(2, 4, 8)
        addTri(2, 8, 3)
        addTri(3, 8, 9)
        addTri(3, 9, 13)
        addTri(13, 9, 12)
        addTri(13, 12, 11)
        addTri(11, 12, 10)
        addTri(11, 10, 7)
        addTri(7, 10, 6)
        addTri(7, 6, 5)
        addTri(5, 6, 4)

        addTri(10, 12, 6)
        addTri(6, 12, 9)

        addTri(9, 8, 6)
        addTri(6, 8, 4)

        addTri(1, 0, 3)
        addTri(3, 0, 2)

        draw()
    }

    override fun inView(): Boolean {
        val (x, y, z) = currentParams
        return false ||
            Frustum.test(x + 0.5, y + 0.5, z + 0.5) ||
            Frustum.test(x + 0.5, y + 0.5, z - 0.5) ||
            Frustum.test(x + 0.5, y - 0.5, z + 0.5) ||
            Frustum.test(x + 0.5, y - 0.5, z - 0.5) ||
            Frustum.test(x - 0.5, y + 0.5, z + 0.5) ||
            Frustum.test(x - 0.5, y + 0.5, z - 0.5) ||
            Frustum.test(x - 0.5, y - 0.5, z + 0.5) ||
            Frustum.test(x - 0.5, y - 0.5, z - 0.5)
    }

    override fun getVertexCount(): Int = 14
    override fun getIndexCount(): Int = 24 * 3
    override fun getDrawMode(): Int = GL11.GL_TRIANGLES
}