package com.perseuspotter.apelles.outline.shader.jfa

import com.perseuspotter.apelles.depression.Shader
import org.lwjgl.opengl.GL20

object JFAPass : Shader(getResource("/shaders/jfa/jfaPass.frag"), getResource("/shaders/jfa/jfaPass.vert")) {
    fun setGap(gap: Int) {
        GL20.glUniform1i(getUniformLoc("outlineGap"), gap)
    }

    fun setSize(w: Int, h: Int) {
        GL20.glUniform2i(getUniformLoc("dim"), w, h)
    }

    override fun init() {
        this.bind()
        GL20.glUniform1i(getUniformLoc("pingPong"), 0)
    }
}