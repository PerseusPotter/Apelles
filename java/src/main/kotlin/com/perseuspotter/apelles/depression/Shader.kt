package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.state.GlState
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

open class Shader(val fragSrc: String?, val vertSrc: String?) {
    var init = false
    var progId = 0
    val uniformCache = mutableMapOf<String, Int>()

    fun build() {
        init = true

        progId = GL20.glCreateProgram()
        if (progId == 0) return println("Error: Failed to create shader program.")

        var fragId = 0
        if (fragSrc != null) {
            fragId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
            if (fragId == 0) return println("Error: Failed to create fragment shader.")

            GL20.glShaderSource(fragId, fragSrc)
            GL20.glCompileShader(fragId)

            val fragStatus = GL20.glGetShaderi(fragId, GL20.GL_COMPILE_STATUS)
            if (fragStatus == GL11.GL_FALSE) {
                val log = GL20.glGetShaderInfoLog(fragId, 1024)
                println("Fragment Shader Compilation Error:\n$log")
                return
            }

            GL20.glAttachShader(progId, fragId)
        }

        var vertId = 0
        if (vertSrc != null) {
            vertId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
            if (vertId == 0) return println("Error: Failed to create vertex shader.")

            GL20.glShaderSource(vertId, vertSrc)
            GL20.glCompileShader(vertId)

            val vertStatus = GL20.glGetShaderi(vertId, GL20.GL_COMPILE_STATUS)
            if (vertStatus == GL11.GL_FALSE) {
                val log = GL20.glGetShaderInfoLog(vertId, 1024)
                println("Vertex Shader Compilation Error:\n$log")
                return
            }

            GL20.glAttachShader(progId, vertId)
        }

        GL20.glLinkProgram(progId)

        val linkStatus = GL20.glGetProgrami(progId, GL20.GL_LINK_STATUS)
        if (linkStatus == GL11.GL_FALSE) {
            val log = GL20.glGetProgramInfoLog(progId, 1024)
            println("Shader Program Linking Error:\n$log")
            return
        }

        GL20.glValidateProgram(progId)

        val validateStatus = GL20.glGetProgrami(progId, GL20.GL_VALIDATE_STATUS)
        if (validateStatus == GL11.GL_FALSE) {
            val log = GL20.glGetProgramInfoLog(progId, 1024)
            println("Shader Program Validation Error:\n$log")
            return
        }

        if (fragId != 0) GL20.glDeleteShader(fragId)
        if (vertId != 0) GL20.glDeleteShader(vertId)

        init()
    }


    open fun init() {}

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