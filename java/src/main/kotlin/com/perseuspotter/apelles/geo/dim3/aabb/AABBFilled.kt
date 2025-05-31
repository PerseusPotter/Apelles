package com.perseuspotter.apelles.geo.dim3.aabb

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.component6
import org.lwjgl.opengl.GL11

object AABBFilled : Geometry() {
    override val name = "aabbF"
    override fun render(pt: Double, params: List<Double>) {
        val (_x1, _y1, _z1, _x2, _y2, _z2) = params
        val (x1, y1, z1, s) = rescale(_x1, _y1, _z1)
        val x2 = x1 + (_x2 - _x1) * s
        val y2 = y1 + (_y2 - _y1) * s
        val z2 = z1 + (_z2 - _z1) * s

        begin(GL11.GL_TRIANGLE_STRIP, false, (x1 + x2) / 2.0, (y1 + y2) / 2.0, (z1 + z2) / 2.0)
        if (Renderer.USE_NEW_SHIT) {
            addVert(x1, y1, z1) // 0
            addVert(x1, y1, z2) // 1
            addVert(x1, y2, z1) // 2
            addVert(x1, y2, z2) // 3
            addVert(x2, y1, z1) // 4
            addVert(x2, y1, z2) // 5
            addVert(x2, y2, z1) // 6
            addVert(x2, y2, z2) // 7

            index(0)
            index(2)
            index(4)
            index(6)

            index(5)
            index(7)

            index(1)
            index(3)

            index(0)
            index(2)

            reset()
            index(2)
            index(3)
            index(6)
            index(7)

            reset()
            index(1)
            index(0)
            index(5)
            index(4)
        } else {
            pos(x1, y1, z1)
            pos(x1, y2, z1)
            pos(x2, y1, z1)
            pos(x2, y2, z1)

            pos(x2, y1, z2)
            pos(x2, y2, z2)

            pos(x1, y1, z2)
            pos(x1, y2, z2)

            pos(x1, y1, z1)
            pos(x1, y2, z1)

            pos(x1, y2, z1)

            pos(x1, y2, z2)
            pos(x2, y2, z1)
            pos(x2, y2, z2)

            pos(x2, y2, z2)
            pos(x1, y1, z2)

            pos(x1, y1, z2)
            pos(x1, y1, z1)
            pos(x2, y1, z2)
            pos(x2, y1, z1)
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean {
        val (x1, y1, z1, x2, y2, z2) = params
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

    override fun getVertexCount(params: List<Double>): Int = 8
    override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 20 else 20
    override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
}