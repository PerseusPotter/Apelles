package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.GeometryInternal
import com.perseuspotter.apelles.state.Color
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11

object PrimitiveColorInternal : GeometryInternal() {
    override val name = "primitivecolorinternal"
    override fun render(pt: Double, params: DoubleArray, N: Int) {
        if (Renderer.USE_NEW_SHIT) {
            begin(GL11.GL_TRIANGLES, false, params[0], params[1], params[2])
            var v = 0
            for (i in 0 until N step 16) {
                val r = params[i + 0]
                val g = params[i + 1]
                val b = params[i + 2]
                val a = params[i + 3]
                currCol = Color(r, g, b, a)
                for (k in 0 until 4) addVert(params[i + 4 + 3 * k], params[i + 5 + 3 * k], params[i + 6 + 3 * k])
                index(v + 0)
                index(v + 1)
                index(v + 2)
                index(v + 2)
                index(v + 1)
                index(v + 3)
                v += 4
            }
            draw()
        } else {
            worldRen.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR)
            for (i in 0 until N step 16) {
                val r = params[i + 0].toFloat()
                val g = params[i + 1].toFloat()
                val b = params[i + 2].toFloat()
                val a = params[i + 3].toFloat()
                worldRen.pos(params[i + 4] - rxc, params[i + 5] - ryc, params[i + 6] - rzc).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 7] - rxc, params[i + 8] - ryc, params[i + 9] - rzc).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 10] - rxc, params[i + 11] - ryc, params[i + 12] - rzc).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 10] - rxc, params[i + 11] - ryc, params[i + 12] - rzc).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 7] - rxc, params[i + 8] - ryc, params[i + 9] - rzc).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 13] - rxc, params[i + 14] - ryc, params[i + 15] - rzc).color(r, g, b, a).endVertex()
            }
            draw()
        }
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: DoubleArray, N: Int): Int = N / 16 * 6
    override fun getIndicesCount(params: DoubleArray, N: Int): Int = N / 16 * 6
    override fun getDrawMode(params: DoubleArray, N: Int): Int = GL11.GL_TRIANGLES
}