package com.perseuspotter.apelles.outline.shader.blur

import com.perseuspotter.apelles.depression.ChromaShader
import com.perseuspotter.apelles.depression.Shader
import com.perseuspotter.apelles.state.Color
import org.lwjgl.opengl.GL20

object BlurInit : ChromaShader(getResource("/shaders/blur/blurInit.frag"), getResource("/shaders/blur/blurInit.vert"), true) {
    fun setColor(col: Color) {
        GL20.glUniform4f(getUniformLoc("color"), col.r, col.g, col.b, col.a)
    }
}