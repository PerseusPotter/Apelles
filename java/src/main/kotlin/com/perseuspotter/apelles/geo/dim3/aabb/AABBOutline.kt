package com.perseuspotter.apelles.geo.dim3.aabb

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.geo.component6
import org.lwjgl.opengl.GL11

object AABBOutline : Geometry() {
    override val name = "aabbO"
    override fun render(pt: Double, params: List<Double>) {
        val (_x1, _y1, _z1, _x2, _y2, _z2) = params
        val (x1, y1, z1, s) = rescale(_x1, _y1, _z1)
        val x2 = x1 + (_x2 - _x1) * s
        val y2 = y1 + (_y2 - _y1) * s
        val z2 = z1 + (_z2 - _z1) * s

        if (Renderer.USE_NEW_SHIT) {
            begin(GL11.GL_LINE_STRIP, false, (x1 + x2) / 2.0, (y1 + y2) / 2.0, (z1 + z2) / 2.0)
            addVert(x1, y1, z1) // 0
            addVert(x1, y1, z2) // 1
            addVert(x1, y2, z1) // 2
            addVert(x1, y2, z2) // 3
            addVert(x2, y1, z1) // 4
            addVert(x2, y1, z2) // 5
            addVert(x2, y2, z1) // 6
            addVert(x2, y2, z2) // 7

            index(0)
            index(1)
            index(3)
            index(2)
            index(0)
            index(4)
            index(6)
            index(2)
            reset()
            index(1)
            index(5)
            index(7)
            index(3)
            reset()
            index(7)
            index(6)
            reset()
            index(5)
            index(4)
        } else {
            begin(GL11.GL_LINES, false, (x1 + x2) / 2.0, (y1 + y2) / 2.0, (z1 + z2) / 2.0)
            pos(x1, y1, z1); pos(x2, y1, z1)
            pos(x1, y2, z1); pos(x2, y2, z1)
            pos(x1, y1, z1); pos(x1, y2, z1)
            pos(x2, y1, z1); pos(x2, y2, z1)
            pos(x1, y1, z2); pos(x2, y1, z2)
            pos(x1, y2, z2); pos(x2, y2, z2)
            pos(x1, y1, z2); pos(x1, y2, z2)
            pos(x2, y1, z2); pos(x2, y2, z2)
            pos(x1, y1, z1); pos(x1, y1, z2)
            pos(x1, y2, z1); pos(x1, y2, z2)
            pos(x2, y1, z1); pos(x2, y1, z2)
            pos(x2, y2, z1); pos(x2, y2, z2)
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
    override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 19 else 24
    override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_LINE_STRIP else GL11.GL_LINES
}