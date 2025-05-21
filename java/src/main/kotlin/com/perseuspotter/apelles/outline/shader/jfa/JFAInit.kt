package com.perseuspotter.apelles.outline.shader.jfa

import com.perseuspotter.apelles.depression.Shader
import org.lwjgl.opengl.GL20

object JFAInit : Shader(getResource("/shaders/jfa/jfaInit.frag"), getResource("/shaders/jfa/jfaInit.vert")) {
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