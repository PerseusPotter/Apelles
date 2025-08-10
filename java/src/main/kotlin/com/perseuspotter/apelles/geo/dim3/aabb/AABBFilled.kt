package com.perseuspotter.apelles.geo.dim3.aabb

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.component6
import org.lwjgl.opengl.GL11

object AABBFilled : Geometry() {
    override val name = "aabbF"
    override fun render(pt: Double) {
        val (_x1, _y1, _z1, _x2, _y2, _z2) = currentParams
        val (x1, y1, z1, s) = rescale(_x1, _y1, _z1)
        val x2 = x1 + (_x2 - _x1) * s
        val y2 = y1 + (_y2 - _y1) * s
        val z2 = z1 + (_z2 - _z1) * s

        begin(GL11.GL_TRIANGLES, false)

        addVert(x1, y1, z1) // 0
        addVert(x1, y1, z2) // 1
        addVert(x1, y2, z1) // 2
        addVert(x1, y2, z2) // 3
        addVert(x2, y1, z1) // 4
        addVert(x2, y1, z2) // 5
        addVert(x2, y2, z1) // 6
        addVert(x2, y2, z2) // 7

        addTri(0, 2, 4)
        addTri(4, 2, 6)
        addTri(4, 6, 5)
        addTri(5, 6, 7)
        addTri(5, 7, 1)
        addTri(1, 7, 3)
        addTri(1, 3, 0)
        addTri(0, 3, 2)
        addTri(2, 3, 6)
        addTri(6, 3, 7)
        addTri(1, 0, 5)
        addTri(5, 0, 4)

        draw()
    }

    override fun inView(): Boolean {
        val (x1, y1, z1, x2, y2, z2) = currentParams
        return false ||
            Frustum.test(x1, y1, z1) ||
            Frustum.test(x1, y1, z2) ||
            Frustum.test(x1, y2, z1) ||
            Frustum.test(x1, y2, z2) ||
            Frustum.test(x2, y1, z1) ||
            Frustum.test(x2, y1, z2) ||
            Frustum.test(x2, y2, z1) ||
            Frustum.test(x2, y2, z2)
    }

    override fun getVertexCount(): Int = 8
    override fun getIndexCount(): Int = 12 * 3
    override fun getDrawMode(): Int = GL11.GL_TRIANGLES
}