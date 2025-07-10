package com.perseuspotter.apelles.geo.dim3

import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.opengl.GL11

object Billboard : Geometry() {
    override val name = "billboard"
    override fun render(pt: Double, params: List<Double>) {
        val (_x, _y, _z, _w, _h) = params
        val (x, y, z, s) = rescale(_x, _y, _z)
        val w = _w * s * 0.5
        val h = _h * s * 0.5

        begin(GL11.GL_TRIANGLE_STRIP, false, x, y, z)
        pos(
            x + w * getCameraRV().x + h * getCameraUV().x,
            y + w * getCameraRV().y + h * getCameraUV().y,
            z + w * getCameraRV().z + h * getCameraUV().z
        )
        pos(
            x - w * getCameraRV().x + h * getCameraUV().x,
            y - w * getCameraRV().y + h * getCameraUV().y,
            z - w * getCameraRV().z + h * getCameraUV().z
        )
        pos(
            x + w * getCameraRV().x - h * getCameraUV().x,
            y + w * getCameraRV().y - h * getCameraUV().y,
            z + w * getCameraRV().z - h * getCameraUV().z
        )
        pos(
            x - w * getCameraRV().x - h * getCameraUV().x,
            y - w * getCameraRV().y - h * getCameraUV().y,
            z - w * getCameraRV().z - h * getCameraUV().z
        )
        draw()
    }

    override fun inView(params: List<Double>): Boolean = true

    override fun getVertexCount(params: List<Double>): Int = 4
    override fun getIndicesCount(params: List<Double>): Int = 4
    override fun getDrawMode(params: List<Double>): Int = GL11.GL_TRIANGLE_STRIP
}