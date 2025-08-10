package com.perseuspotter.apelles.geo.dim3.stair.inner

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object StairInnerFilled : Geometry() {
    override val name: String = "stairInnerF"
    override fun render(pt: Double) {
        val (_x, _y, _z, _c) = currentParams
        val (x, y, z, s) = rescale(_x, _y , _z)
        val verts = OrientationInner.vertices[_c.toInt()]

        begin(GL11.GL_TRIANGLES, false)

        verts.forEach { addVert(x + it.x * s, y + it.y * s, z + it.z * s) }

        addTri(10, 12, 2)
        addTri(10, 2, 6)
        addTri(6, 2, 0)
        addTri(6, 0, 4)
        addTri(4, 0, 1)
        addTri(4, 1, 5)
        addTri(5, 1, 9)
        addTri(5, 9, 8)
        addTri(8, 9, 13)
        addTri(8, 13, 11)
        addTri(11, 13, 12)
        addTri(11, 12, 10)

        addTri(11, 10, 7)
        addTri(7, 10, 6)
        addTri(7, 6, 5)
        addTri(5, 6, 4)

        addTri(8, 11, 5)
        addTri(5, 11, 7)

        addTri(12, 13, 2)
        addTri(2, 13, 3)
        addTri(2, 3, 0)
        addTri(0, 3, 1)

        addTri(13, 9, 3)
        addTri(3, 9, 1)

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