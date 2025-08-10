package com.perseuspotter.apelles.geo.dim3.aabb

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.component6
import com.perseuspotter.apelles.geo.component7
import org.lwjgl.opengl.GL11

object AABBOutlineJoined : Geometry() {
    override val name = "aabbOJ"
    override fun render(pt: Double) {
        val (_x1, _y1, _z1, _x2, _y2, _z2, _lw) = currentParams
        val (x1, y1, z1, s) = rescale(_x1, _y1, _z1)
        val x2 = x1 + (_x2 - _x1) * s
        val y2 = y1 + (_y2 - _y1) * s
        val z2 = z1 + (_z2 - _z1) * s
        val lw = _lw * s * 0.5 * 0.0625

        begin(GL11.GL_TRIANGLES, false)

        // 0
        addVert(x1 - lw, y1 - lw, z1 - lw)
        addVert(x1 - lw, y1 - lw, z2 + lw)
        addVert(x2 + lw, y1 - lw, z1 - lw)
        addVert(x2 + lw, y1 - lw, z2 + lw)
        // 4
        addVert(x1 + lw, y1 - lw, z1 + lw)
        addVert(x1 + lw, y1 - lw, z2 - lw)
        addVert(x2 - lw, y1 - lw, z1 + lw)
        addVert(x2 - lw, y1 - lw, z2 - lw)
        // 8
        addVert(x1 - lw, y1 + lw, z1 + lw)
        addVert(x1 - lw, y1 + lw, z2 - lw)
        addVert(x1 + lw, y1 + lw, z1 - lw)
        addVert(x1 + lw, y1 + lw, z1 + lw)
        addVert(x1 + lw, y1 + lw, z2 - lw)
        addVert(x1 + lw, y1 + lw, z2 + lw)
        // 14
        addVert(x2 - lw, y1 + lw, z1 - lw)
        addVert(x2 - lw, y1 + lw, z1 + lw)
        addVert(x2 - lw, y1 + lw, z2 - lw)
        addVert(x2 - lw, y1 + lw, z2 + lw)
        addVert(x2 + lw, y1 + lw, z1 + lw)
        addVert(x2 + lw, y1 + lw, z2 - lw)
        // 20
        addVert(x1 - lw, y2 - lw, z1 + lw)
        addVert(x1 - lw, y2 - lw, z2 - lw)
        addVert(x1 + lw, y2 - lw, z1 - lw)
        addVert(x1 + lw, y2 - lw, z1 + lw)
        addVert(x1 + lw, y2 - lw, z2 - lw)
        addVert(x1 + lw, y2 - lw, z2 + lw)
        // 26
        addVert(x2 - lw, y2 - lw, z1 - lw)
        addVert(x2 - lw, y2 - lw, z1 + lw)
        addVert(x2 - lw, y2 - lw, z2 - lw)
        addVert(x2 - lw, y2 - lw, z2 + lw)
        addVert(x2 + lw, y2 - lw, z1 + lw)
        addVert(x2 + lw, y2 - lw, z2 - lw)
        // 32
        addVert(x1 - lw, y2 + lw, z1 - lw)
        addVert(x1 - lw, y2 + lw, z2 + lw)
        addVert(x2 + lw, y2 + lw, z1 - lw)
        addVert(x2 + lw, y2 + lw, z2 + lw)
        // 36
        addVert(x1 + lw, y2 + lw, z1 + lw)
        addVert(x1 + lw, y2 + lw, z2 - lw)
        addVert(x2 - lw, y2 + lw, z1 + lw)
        addVert(x2 - lw, y2 + lw, z2 - lw)

        addTri(0, 1, 8)
        addTri(8, 1, 9)
        addTri(8, 9, 11)
        addTri(11, 9, 12)
        addTri(11, 12, 4)
        addTri(4, 12, 5)
        addTri(4, 5, 0)
        addTri(0, 5, 1)

        addTri(1, 3, 13)
        addTri(13, 3, 17)
        addTri(13, 17, 12)
        addTri(12, 17, 16)
        addTri(12, 16, 5)
        addTri(5, 16, 7)
        addTri(5, 7, 1)
        addTri(1, 7, 3)

        addTri(3, 2, 19)
        addTri(19, 2, 18)
        addTri(19, 18, 16)
        addTri(16, 18, 15)
        addTri(16, 15, 7)
        addTri(7, 15, 6)
        addTri(7, 6, 3)
        addTri(3, 6, 2)

        addTri(2, 0, 14)
        addTri(14, 0, 10)
        addTri(14, 10, 15)
        addTri(15, 10, 11)
        addTri(15, 11, 6)
        addTri(6, 11, 4)
        addTri(6, 4, 2)
        addTri(2, 4, 0)

        addTri(0, 32, 10)
        addTri(10, 32, 22)
        addTri(10, 22, 11)
        addTri(11, 22, 23)
        addTri(11, 23, 8)
        addTri(8, 23, 20)
        addTri(8, 20, 0)
        addTri(0, 20, 32)

        addTri(1, 33, 9)
        addTri(9, 33, 21)
        addTri(9, 21, 12)
        addTri(12, 21, 24)
        addTri(12, 24, 13)
        addTri(13, 24, 25)
        addTri(13, 25, 1)
        addTri(1, 25, 33)

        addTri(3, 35, 17)
        addTri(17, 35, 29)
        addTri(17, 29, 16)
        addTri(16, 29, 28)
        addTri(16, 28, 19)
        addTri(19, 28, 31)
        addTri(19, 31, 3)
        addTri(3, 31, 35)

        addTri(2, 34, 18)
        addTri(18, 34, 30)
        addTri(18, 30, 15)
        addTri(15, 30, 27)
        addTri(15, 27, 14)
        addTri(14, 27, 26)
        addTri(14, 26, 2)
        addTri(2, 26, 34)

        addTri(32, 33, 36)
        addTri(36, 33, 37)
        addTri(36, 37, 23)
        addTri(23, 37, 24)
        addTri(23, 24, 20)
        addTri(20, 24, 21)
        addTri(20, 21, 32)
        addTri(32, 21, 33)

        addTri(33, 35, 37)
        addTri(37, 35, 39)
        addTri(37, 39, 24)
        addTri(24, 39, 28)
        addTri(24, 28, 25)
        addTri(25, 28, 29)
        addTri(25, 29, 33)
        addTri(33, 29, 35)

        addTri(35, 34, 39)
        addTri(39, 34, 38)
        addTri(39, 38, 28)
        addTri(28, 38, 27)
        addTri(28, 27, 31)
        addTri(31, 27, 30)
        addTri(31, 30, 35)
        addTri(35, 30, 34)

        addTri(34, 32, 38)
        addTri(38, 32, 36)
        addTri(38, 36, 27)
        addTri(27, 36, 23)
        addTri(27, 23, 26)
        addTri(26, 23, 22)
        addTri(26, 22, 34)
        addTri(34, 22, 32)

        draw()
    }

    override fun inView(): Boolean {
        val (x1, y1, z1, x2, y2, z2, _lw) = currentParams
        val lw = _lw * 0.5 * 0.0625
        return false ||
            Frustum.test(x1 - lw, y1 - lw, z1 - lw) ||
            Frustum.test(x1 - lw, y1 - lw, z2 + lw) ||
            Frustum.test(x1 - lw, y2 + lw, z1 - lw) ||
            Frustum.test(x1 - lw, y2 + lw, z2 + lw) ||
            Frustum.test(x2 + lw, y1 - lw, z1 - lw) ||
            Frustum.test(x2 + lw, y1 - lw, z2 + lw) ||
            Frustum.test(x2 + lw, y2 + lw, z1 - lw) ||
            Frustum.test(x2 + lw, y2 + lw, z2 + lw)
    }

    override fun getVertexCount(): Int = 40
    override fun getIndexCount(): Int = 96 * 3
    override fun getDrawMode(): Int = GL11.GL_TRIANGLES
}