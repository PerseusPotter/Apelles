package com.perseuspotter.apelles.geo.dim3.beacon

import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object BeaconTopOutside : Geometry() {
    override val name = "beaconTO"
    override fun render(pt: Double) {
        val (x, y1, z, h, s) = currentParams

        val x1 = x - 0.3 * s
        val x2 = x + 0.3 * s
        val z1 = z - 0.3 * s
        val z2 = z + 0.3 * s
        val y2 = y1 + h

        begin(GL11.GL_TRIANGLES, false)

        addVert(x1, y1, z1) // 0
        addVert(x2, y1, z1) // 1
        addVert(x1, y1, z2) // 2
        addVert(x2, y1, z2) // 3
        addVert(x1, y2, z2) // 4
        addVert(x2, y2, z2) // 5
        addVert(x1, y2, z1) // 6
        addVert(x2, y2, z1) // 7

        addTri(0, 1, 2)
        addTri(2, 1, 3)

        addTri(4, 5, 6)
        addTri(6, 5, 7)

        draw()
    }

    override fun inView(): Boolean = true

    override fun getVertexCount(): Int = 8
    override fun getIndexCount(): Int = 4 * 3
    override fun getDrawMode(): Int = GL11.GL_TRIANGLES
}