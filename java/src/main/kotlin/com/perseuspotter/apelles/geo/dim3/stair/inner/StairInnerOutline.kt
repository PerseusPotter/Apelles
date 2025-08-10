package com.perseuspotter.apelles.geo.dim3.stair.inner

import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object StairInnerOutline : Geometry() {
    override val name: String = "stairInnerO"
    override fun render(pt: Double) {
        val (_x, _y, _z, _c) = currentParams
        val (x, y, z, s) = rescale(_x, _y , _z)
        val verts = OrientationInner.vertices[_c.toInt()]

        begin(GL11.GL_LINES, false)

        verts.forEach { addVert(x + it.x * s, y + it.y * s, z + it.z * s) }

        emitVert(0); emitVert(1)
        emitVert(1); emitVert(3)
        emitVert(3); emitVert(2)
        emitVert(2); emitVert(0)
        emitVert(0); emitVert(4)
        emitVert(4); emitVert(5)
        emitVert(5); emitVert(8)
        emitVert(8); emitVert(9)
        emitVert(9); emitVert(1)

        emitVert(4); emitVert(6)
        emitVert(6); emitVert(7)
        emitVert(7); emitVert(5)

        emitVert(6); emitVert(10)
        emitVert(10); emitVert(11)
        emitVert(11); emitVert(7)

        emitVert(9); emitVert(13)
        emitVert(13); emitVert(12)
        emitVert(12); emitVert(2)

        emitVert(11); emitVert(8)
        emitVert(3); emitVert(13)
        emitVert(10); emitVert(12)

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
    override fun getIndexCount(): Int = 21 * 2
    override fun getDrawMode(): Int = GL11.GL_LINES
}