package com.perseuspotter.apelles.outline.shader.sobel

import com.perseuspotter.apelles.depression.Shader
import com.perseuspotter.apelles.state.Color
import org.lwjgl.opengl.GL20

object SobelInit : Shader(getResource("/shaders/sobel/sobelInit.frag"), getResource("/shaders/sobel/sobelInit.vert")) {
    fun setColor(col: Color) {
        GL20.glUniform4f(getUniformLoc("color"), col.r, col.g, col.b, col.a)
    }
}