package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.state.GlState
import org.lwjgl.opengl.GL20

class Shader(type: Int) {
    val color = type and 4 == 4
    val norm = type and 2 == 2
    val tex = type and 1 == 1
    var init = false
    var progId = 0
    var vertId = 0

    companion object {
        val store = Array(8) { Shader(it) }

        fun get(c: Boolean, n: Boolean, t: Boolean) = store[(if (c) 4 else 0) or (if (n) 2 else 0) or (if (t) 1 else 0)]
    }

    fun build() {
        init = true
        progId = GL20.glCreateProgram()
        vertId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        GL20.glShaderSource(vertId, javaClass.getResourceAsStream("/shaders/${name()}.glsl")!!.bufferedReader().use { it.readText() })
        GL20.glCompileShader(vertId)
        GL20.glAttachShader(progId, vertId)
        GL20.glLinkProgram(progId)
        GL20.glDeleteShader(vertId)
    }

    fun destroy() {
        GL20.glDeleteProgram(progId)
    }

    fun bind() {
        if (!init) build()
        GlState.bindShader(progId)
    }

    fun unbind() {
        GlState.bindShader(0)
    }

    fun name() = "C${if (color) 1 else 0}N${if (norm) 1 else 0}T${if (tex) 1 else 0}"
}