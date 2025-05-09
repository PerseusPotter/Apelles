package com.perseuspotter.apelles.outline.shader

import com.perseuspotter.apelles.depression.Shader
import org.lwjgl.opengl.GL20

object InitPass : Shader(getResource("/shaders/jfaInit.frag"), getResource("/shaders/jfaInit.vert")) {
    fun setWidth(width: Int) {
        GL20.glUniform1i(getUniformLoc("outlineWidth"), width)
    }

    fun setSize(w: Int, h: Int) {
        GL20.glUniform2f(getUniformLoc("dim"), w.toFloat(), h.toFloat())
    }

    fun setColorId(color: Int) {
        GL20.glUniform1i(getUniformLoc("colorId"), color)
    }
}