package com.perseuspotter.apelles.outline.shader

import com.perseuspotter.apelles.depression.Shader
import com.perseuspotter.apelles.state.Color
import org.lwjgl.opengl.GL20

object OutlineInit : Shader(getResource("/shaders/outlineInit.frag"), getResource("/shaders/outlineInit.vert")) {
    fun setColor(col: Color) {
        GL20.glUniform4f(getUniformLoc("color"), col.r, col.g, col.b, col.a)
    }
}