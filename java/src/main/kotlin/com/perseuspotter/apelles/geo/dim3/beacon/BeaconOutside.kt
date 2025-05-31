package com.perseuspotter.apelles.geo.dim3.beacon

import com.perseuspotter.apelles.geo.Geometry
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import kotlin.math.ceil

object BeaconOutside : Geometry() {
    override val name = "beaconO"
    override fun render(pt: Double, params: List<Double>) {
        val (x, y1, z, h, s) = params

        val time = 0.2 * ((Minecraft.getMinecraft()?.theWorld?.totalWorldTime ?: 0L) + pt)
        val v0 = ceil(time) - time - 1
        val v1 = h + v0
        val x1 = x - 0.3 * s
        val x2 = x + 0.3 * s
        val z1 = z - 0.3 * s
        val z2 = z + 0.3 * s
        val y2 = y1 + h

        begin(GL11.GL_TRIANGLE_STRIP, true, x, y1 + h / 2.0, z)
        pos(x1, y1, z1, 0.0, v0)
        pos(x1, y2, z1, 0.0, v1)
        pos(x2, y1, z1, 1.0, v0)
        pos(x2, y2, z1, 1.0, v1)

        pos(x2, y1, z2, 2.0, v0)
        pos(x2, y2, z2, 2.0, v1)

        pos(x1, y1, z2, 3.0, v0)
        pos(x1, y2, z2, 3.0, v1)

        pos(x1, y1, z1, 4.0, v0)
        pos(x1, y2, z1, 4.0, v1)
        draw()
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: List<Double>): Int = 10
    override fun getIndicesCount(params: List<Double>): Int = 10
    override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
}