package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.geo.GeometryInternal
import com.perseuspotter.apelles.state.Color
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11

object PrimitiveColorUVInternal : GeometryInternal() {
    override val name = "primitivecoloruvinternal"
    override fun render(pt: Double, params: DoubleArray, N: Int) {
        if (Renderer.USE_NEW_SHIT) {
            begin(GL11.GL_TRIANGLES, false, params[0], params[1], params[2])
            var v = 0
            for (i in 0 until N step 24) {
                val r = params[i + 0]
                val g = params[i + 1]
                val b = params[i + 2]
                val a = params[i + 3]
                currCol = Color(r, g, b, a)
                for (k in 0 until 4) addVert(params[i + 4 + 5 * k], params[i + 5 + 5 * k], params[i + 6 + 5 * k], params[i + 7 + 5 * k], params[i + 8 + 5 * k])
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
            worldRen.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_COLOR)
            for (i in 0 until N step 24) {
                val r = params[i + 0].toFloat()
                val g = params[i + 1].toFloat()
                val b = params[i + 2].toFloat()
                val a = params[i + 3].toFloat()
                worldRen.pos(params[i + 4] - rxc, params[i + 5] - ryc, params[i + 6] - rzc).tex(params[i + 7], params[i + 8]).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 9] - rxc, params[i + 10] - ryc, params[i + 11] - rzc).tex(params[i + 12], params[i + 13]).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 14] - rxc, params[i + 15] - ryc, params[i + 16] - rzc).tex(params[i + 17], params[i + 18]).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 14] - rxc, params[i + 15] - ryc, params[i + 16] - rzc).tex(params[i + 17], params[i + 18]).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 9] - rxc, params[i + 10] - ryc, params[i + 11] - rzc).tex(params[i + 12], params[i + 13]).color(r, g, b, a).endVertex()
                worldRen.pos(params[i + 19] - rxc, params[i + 20] - ryc, params[i + 21] - rzc).tex(params[i + 22], params[i + 23]).color(r, g, b, a).endVertex()
            }
            draw()
        }
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: DoubleArray, N: Int): Int = N / 24 * 6
    override fun getIndicesCount(params: DoubleArray, N: Int): Int = N / 24 * 6
    override fun getDrawMode(params: DoubleArray, N: Int): Int = GL11.GL_TRIANGLES
}