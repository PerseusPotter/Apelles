package com.perseuspotter.apelles.geo.dim3.beacon

import com.perseuspotter.apelles.geo.Geometry
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

object BeaconInside : Geometry() {
    override val name = "beaconI"
    override fun render(pt: Double) {
        val (x, y1, z, h, s) = currentParams

        val time = 0.2 * ((Minecraft.getMinecraft()?.theWorld?.totalWorldTime ?: 0L) + pt)
        val v0 = ceil(time) - time - 1
        val v1 = h * 2.5 + v0
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

        begin(GL11.GL_TRIANGLES, true)

        addVert(x + d0, y1, z + d1, 0.0, v0) // 0
        addVert(x + d0, y2, z + d1, 0.0, v1) // 1
        addVert(x + d2, y1, z + d3, 1.0, v0) // 2
        addVert(x + d2, y2, z + d3, 1.0, v1) // 3
        addVert(x + d4, y1, z + d5, 2.0, v0) // 4
        addVert(x + d4, y2, z + d5, 2.0, v1) // 5
        addVert(x + d6, y1, z + d7, 3.0, v0) // 6
        addVert(x + d6, y2, z + d7, 3.0, v1) // 7
        addVert(x + d0, y1, z + d1, 4.0, v0) // 8
        addVert(x + d0, y2, z + d1, 4.0, v1) // 9

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