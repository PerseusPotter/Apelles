package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.geo.GeometryInternal
import org.lwjgl.opengl.GL11

object PrimitiveColorInternal : GeometryInternal() {
    override val name = "primitivecolorinternal"
    override fun render(pt: Double, params: DoubleArray, N: Int) {
        begin(GL11.GL_TRIANGLES, false, true)

        var v = 0
        for (i in 0 until N step 16) {
            val r = params[i + 0]
            val g = params[i + 1]
            val b = params[i + 2]
            val a = params[i + 3]

            for (k in 0 until 4) addVert(
                params[i + 4 + 3 * k],
                params[i + 5 + 3 * k],
                params[i + 6 + 3 * k],
                r.toFloat(),
                g.toFloat(),
                b.toFloat(),
                a.toFloat()
            )
            addTri(v + 0, v + 1, v + 2)
            addTri(v + 2, v + 1, v + 3)

            v += 4
        }

        draw()
    }

    override fun inView(params: DoubleArray, N: Int): Boolean = true

    override fun getVertexCount(params: DoubleArray, N: Int): Int = N / 16 * 4
    override fun getIndexCount(params: DoubleArray, N: Int): Int = N / 16 * 2 * 3
    override fun getDrawMode(params: DoubleArray, N: Int): Int = GL11.GL_TRIANGLES
}