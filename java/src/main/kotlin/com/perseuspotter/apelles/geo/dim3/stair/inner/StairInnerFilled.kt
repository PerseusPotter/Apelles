package com.perseuspotter.apelles.geo.dim3.stair.inner

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object StairInnerFilled : Geometry() {
    override val name: String = "stairInnerF"
    override fun render(pt: Double, params: List<Double>) {
        val (_x, _y, _z, _c) = params
        val (x, y, z, s) = rescale(_x, _y , _z)
        val verts = OrientationInner.vertices[_c.toInt()]

        begin(GL11.GL_TRIANGLE_STRIP, false, x + verts[0].x * s, y + verts[0].y * s, z + verts[0].z * s)
        if (Renderer.USE_NEW_SHIT) {
            verts.forEachIndexed { i, v -> if (i > 0) addVert(x + v.x * s, y + v.y * s, z + v.z * s) }

            index(12)
            index(12)
            index(10)
            index(2)
            index(6)
            index(0)
            index(4)
            index(1)
            index(5)
            index(9)
            index(8)
            index(13)
            index(11)
            index(12)
            index(10)

            reset()
            index(11)
            index(10)
            index(7)
            index(6)
            index(5)
            index(4)
            reset()
            index(8)
            index(11)
            index(5)
            index(7)

            reset()
            index(12)
            index(13)
            index(2)
            index(3)
            index(0)
            index(1)
            reset()
            index(13)
            index(9)
            index(3)
            index(1)
        } else {
            fun emit(i: Int) = pos(x + verts[i + 1].x * s, y + verts[i + 1].y * s, z + verts[i + 1].z * s)

            emit(12)
            emit(12)
            emit(10)
            emit(2)
            emit(6)
            emit(0)
            emit(4)
            emit(1)
            emit(5)
            emit(9)
            emit(8)
            emit(13)
            emit(11)
            emit(12)
            emit(10)

            emit(10)
            emit(11)
            emit(6)
            emit(7)
            emit(4)
            emit(5)
            emit(5)
            emit(7)
            emit(8)
            emit(11)

            emit(11)
            emit(9)
            emit(9)
            emit(13)
            emit(1)
            emit(3)
            emit(0)
            emit(2)
            emit(2)
            emit(3)
            emit(12)
            emit(13)
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean {
        val (x, y, z) = params
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

    override fun getVertexCount(params: List<Double>): Int = 14
    override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 39 else 37
    override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
}