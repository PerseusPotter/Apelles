package com.perseuspotter.apelles.geo.dim3.beacon

import com.perseuspotter.apelles.geo.Geometry
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import kotlin.math.ceil

object BeaconOutside : Geometry() {
    override val name = "beaconO"
    override fun render(pt: Double) {
        val (x, y1, z, h, s) = currentParams

        val time = 0.2 * ((Minecraft.getMinecraft()?.theWorld?.totalWorldTime ?: 0L) + pt)
        val v0 = ceil(time) - time - 1
        val v1 = h + v0
        val x1 = x - 0.3 * s
        val x2 = x + 0.3 * s
        val z1 = z - 0.3 * s
        val z2 = z + 0.3 * s
        val y2 = y1 + h

        begin(GL11.GL_TRIANGLES, true)

        addVert(x1, y1, z1, 0.0, v0) // 0
        addVert(x1, y2, z1, 0.0, v1) // 1
        addVert(x2, y1, z1, 1.0, v0) // 2
        addVert(x2, y2, z1, 1.0, v1) // 3
        addVert(x2, y1, z2, 2.0, v0) // 4
        addVert(x2, y2, z2, 2.0, v1) // 5
        addVert(x1, y1, z2, 3.0, v0) // 6
        addVert(x1, y2, z2, 3.0, v1) // 7
        addVert(x1, y1, z1, 4.0, v0) // 8
        addVert(x1, y2, z1, 4.0, v1) // 9

        addTri(0, 1, 2)
        addTri(2, 1, 3)
        addTri(2, 3, 4)
        addTri(4, 3, 5)
        addTri(4, 5, 6)
        addTri(6, 5, 7)
        addTri(6, 7, 8)
        addTri(8, 7, 9)

        draw()
    }

    override fun inView(): Boolean = true

    override fun getVertexCount(): Int = 10
    override fun getIndexCount(): Int = 8 * 3
    override fun getDrawMode(): Int = GL11.GL_TRIANGLES
}