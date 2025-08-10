package com.perseuspotter.apelles.geo.dim3.stair.straight

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object StairStraightFilled : Geometry() {
    override val name: String = "stairStraightF"
    override fun render(pt: Double) {
        val (_x, _y, _z, _c) = currentParams
        val (x, y, z, s) = rescale(_x, _y , _z)
        val verts = OrientationStraight.vertices[_c.toInt()]

        begin(GL11.GL_TRIANGLES, false)

        verts.forEach { addVert(x + it.x * s, y + it.y * s, z + it.z * s) }

        addTri(0, 1, 4)
        addTri(4, 1, 5)
        addTri(4, 5, 6)
        addTri(6, 5, 7)
        addTri(6, 7, 8)
        addTri(8, 7, 9)
        addTri(8, 9, 10)
        addTri(10, 9, 11)
        addTri(10, 11, 2)
        addTri(2, 11, 3)
        addTri(2, 3, 0)
        addTri(0, 3, 1)

        addTri(8, 10, 2)
        addTri(8, 2, 6)
        addTri(6, 2, 0)
        addTri(6, 0, 4)

        addTri(11, 9, 3)
        addTri(3, 9, 7)
        addTri(3, 7, 1)
        addTri(1, 7, 5)

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

    override fun getVertexCount(): Int = 12
    override fun getIndexCount(): Int = 20 * 3
    override fun getDrawMode(): Int = GL11.GL_TRIANGLES
}