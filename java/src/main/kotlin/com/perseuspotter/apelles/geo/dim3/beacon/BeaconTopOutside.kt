package com.perseuspotter.apelles.geo.dim3.beacon

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object BeaconTopOutside : Geometry() {
    override val name = "beaconTO"
    override fun render(pt: Double, params: List<Double>) {
        val (x, y1, z, h, s) = params

        val x1 = x - 0.3 * s
        val x2 = x + 0.3 * s
        val z1 = z - 0.3 * s
        val z2 = z + 0.3 * s
        val y2 = y1 + h

        if (Renderer.USE_NEW_SHIT) {
            begin(GL11.GL_TRIANGLE_STRIP, false, x, y1 + h / 2.0, z)
            pos(x1, y1, z1)
            pos(x2, y1, z1)
            pos(x1, y1, z2)
            pos(x2, y1, z2)

            reset()
            pos(x1, y2, z2)
            pos(x2, y2, z2)
            pos(x1, y2, z1)
            pos(x2, y2, z1)
        } else {
            begin(GL11.GL_QUADS, false, x, y1 + h / 2.0, z)
            pos(x1, y1, z1)
            pos(x2, y1, z1)
            pos(x2, y1, z2)
            pos(x1, y1, z2)

            pos(x1, y2, z2)
            pos(x2, y2, z2)
            pos(x2, y2, z1)
            pos(x1, y2, z1)
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: List<Double>): Int = 8
    override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 9 else 8
    override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_TRIANGLE_STRIP else GL11.GL_QUADS
}