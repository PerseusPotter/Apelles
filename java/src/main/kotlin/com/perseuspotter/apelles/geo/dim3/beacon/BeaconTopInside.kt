package com.perseuspotter.apelles.geo.dim3.beacon

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.Geometry
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import kotlin.math.cos
import kotlin.math.sin

object BeaconTopInside : Geometry() {
    override val name = "beaconTI"
    override fun render(pt: Double, params: List<Double>) {
        val (x, y1, z, h, s) = params

        val time = 0.2 * ((Minecraft.getMinecraft()?.theWorld?.totalWorldTime ?: 0L) + pt)
        val t = time * -0.1875
        val d0 = cos(t + Math.PI * 1 / 4) * 0.2 * s
        val d1 = sin(t + Math.PI * 1 / 4) * 0.2 * s
        val d2 = cos(t + Math.PI * 3 / 4) * 0.2 * s
        // val d3 = sin(t1 + Math.PI * 3 / 4) * 0.2 * s
        val d3 = d0
        val d4 = cos(t + Math.PI * 5 / 4) * 0.2 * s
        // val d5 = sin(t1 + Math.PI * 5 / 4) * 0.2 * s
        val d5 = d2
        // val d6 = cos(t + Math.PI * 7 / 4) * 0.2 * s
        val d6 = d1
        // val d7 = sin(t1 + Math.PI * 7 / 4) * 0.2 * s
        val d7 = d4
        val y2 = y1 + h

        if (Renderer.USE_NEW_SHIT) {
            begin(GL11.GL_TRIANGLE_STRIP, false, x, y1 + h / 2.0, z)
            pos(x + d0, y1, z + d1)
            pos(x + d2, y1, z + d3)
            pos(x + d6, y1, z + d7)
            pos(x + d4, y1, z + d5)

            reset()
            pos(x + d6, y2, z + d7)
            pos(x + d4, y2, z + d5)
            pos(x + d0, y2, z + d1)
            pos(x + d2, y2, z + d3)
        } else {
            begin(GL11.GL_QUADS, false, x, y1 + h / 2.0, z)
            pos(x + d0, y1, z + d1)
            pos(x + d2, y1, z + d3)
            pos(x + d4, y1, z + d5)
            pos(x + d6, y1, z + d7)

            pos(x + d6, y2, z + d7)
            pos(x + d4, y2, z + d5)
            pos(x + d2, y2, z + d3)
            pos(x + d0, y2, z + d1)
        }
        draw()
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: List<Double>): Int = 8
    override fun getIndicesCount(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) 9 else 8
    override fun getDrawMode(params: List<Double>): Int = if (Renderer.USE_NEW_SHIT) GL11.GL_TRIANGLE_STRIP else GL11.GL_QUADS
}