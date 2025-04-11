package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.state.GlState
import org.lwjgl.opengl.GL20

open class Shader(val fragSrc: String?, val vertSrc: String?) {
    var init = false
    var progId = 0
    val uniformCache = mutableMapOf<String, Int>()

    fun build() {
        init = true
        progId = GL20.glCreateProgram()
        var fragId = 0
        if (fragSrc != null) {
            fragId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
            GL20.glShaderSource(fragId, fragSrc)
            GL20.glCompileShader(fragId)
            GL20.glAttachShader(progId, fragId)
        }
        var vertId = 0
        if (vertSrc != null) {
            vertId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
            GL20.glShaderSource(vertId, vertSrc)
            GL20.glCompileShader(vertId)
            GL20.glAttachShader(progId, vertId)
        }
        GL20.glLinkProgram(progId)
        if (fragId != 0) GL20.glDeleteShader(fragId)
        if (vertId != 0) GL20.glDeleteShader(vertId)
    }

    fun getUniformLoc(name: String) = uniformCache.getOrPut(name) { GL20.glGetUniformLocation(progId, name) }

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

    companion object {
        @JvmStatic
        protected fun getResource(name: String) = javaClass.getResourceAsStream(name)?.bufferedReader().use { it?.readText() }
    }
}