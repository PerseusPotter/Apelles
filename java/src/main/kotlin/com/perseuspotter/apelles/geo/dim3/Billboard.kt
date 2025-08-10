package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object Billboard : Geometry() {
    override val name = "billboard"
    override fun render(pt: Double) {
        val (_x, _y, _z, _w, _h) = currentParams
        val (x, y, z, s) = rescale(_x, _y, _z)
        val w = _w * s * 0.5
        val h = _h * s * 0.5

        begin(GL11.GL_TRIANGLES, false)

        addVert(
            x + w * getCameraRV().x + h * getCameraUV().x,
            y + w * getCameraRV().y + h * getCameraUV().y,
            z + w * getCameraRV().z + h * getCameraUV().z
        )
        addVert(
            x - w * getCameraRV().x + h * getCameraUV().x,
            y - w * getCameraRV().y + h * getCameraUV().y,
            z - w * getCameraRV().z + h * getCameraUV().z
        )
        addVert(
            x + w * getCameraRV().x - h * getCameraUV().x,
            y + w * getCameraRV().y - h * getCameraUV().y,
            z + w * getCameraRV().z - h * getCameraUV().z
        )
        addVert(
            x - w * getCameraRV().x - h * getCameraUV().x,
            y - w * getCameraRV().y - h * getCameraUV().y,
            z - w * getCameraRV().z - h * getCameraUV().z
        )

        addTri(0, 1, 2)
        addTri(2, 1, 3)

        draw()
    }

    override fun inView(): Boolean = true

    override fun getVertexCount(): Int = 4
    override fun getIndexCount(): Int = 2 * 3
    override fun getDrawMode(): Int = GL11.GL_TRIANGLES
}