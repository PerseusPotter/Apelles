package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.Renderer
import com.perseuspotter.apelles.state.GlState
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL32

open class Shader(val fragSrc: String?, val vertSrc: String?, val geomSrc: String? = null, val createProgram: Boolean = true, val deleteShaders: Boolean = true, var fragId: Int = 0, var vertId: Int = 0, var geomId: Int = 0, val leech: Shader? = null) {
    var init = false
    var postInit = false
    var progId = 0
    val uniformCache = mutableMapOf<String, Int>()

    fun build() {
        init = true

        if (leech != null) {
            fragId = leech.fragId
            vertId = leech.vertId
            geomId = leech.geomId
        }

        if (createProgram) {
            progId = GL20.glCreateProgram()
            if (progId == 0) {
                println("Error: Failed to create shader program.")
                Throwable().printStackTrace()
                return
            }
        }

        if (fragSrc != null) {
            fragId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
            if (fragId == 0) {
                println("Error: Failed to create fragment shader.")
                Throwable().printStackTrace()
                return
            }

            GL20.glShaderSource(fragId, fragSrc)
            GL20.glCompileShader(fragId)

            val fragStatus = GL20.glGetShaderi(fragId, GL20.GL_COMPILE_STATUS)
            if (fragStatus == GL11.GL_FALSE) {
                val log = GL20.glGetShaderInfoLog(fragId, 1024)
                println("Fragment Shader Compilation Error:\n$log")
                Throwable().printStackTrace()
                return
            }
        }
        if (createProgram && fragId != 0) GL20.glAttachShader(progId, fragId)

        if (vertSrc != null) {
            vertId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
            if (vertId == 0) {
                println("Error: Failed to create vertex shader.")
                Throwable().printStackTrace()
                return
            }

            GL20.glShaderSource(vertId, vertSrc)
            GL20.glCompileShader(vertId)

            val vertStatus = GL20.glGetShaderi(vertId, GL20.GL_COMPILE_STATUS)
            if (vertStatus == GL11.GL_FALSE) {
                val log = GL20.glGetShaderInfoLog(vertId, 1024)
                println("Vertex Shader Compilation Error:\n$log")
                Throwable().printStackTrace()
                return
            }
        }
        if (createProgram && vertId != 0) GL20.glAttachShader(progId, vertId)

        if (geomSrc != null && Renderer.CAN_USE_GEOM_SHADER) {
            geomId = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER)
            if (geomId == 0) {
                println("Error: Failed to create geometry shader.")
                Throwable().printStackTrace()
                return
            }

            GL20.glShaderSource(geomId, geomSrc)
            GL20.glCompileShader(geomId)

            val geomStatus = GL20.glGetShaderi(geomId, GL20.GL_COMPILE_STATUS)
            if (geomStatus == GL11.GL_FALSE) {
                val log = GL20.glGetShaderInfoLog(geomId, 1024)
                println("Geometry Shader Compilation Error:\n$log")
                Throwable().printStackTrace()
                return
            }
        }
        if (createProgram && geomId != 0) GL20.glAttachShader(progId, geomId)

        if (createProgram) {
            GL20.glLinkProgram(progId)

            val linkStatus = GL20.glGetProgrami(progId, GL20.GL_LINK_STATUS)
            if (linkStatus == GL11.GL_FALSE) {
                val log = GL20.glGetProgramInfoLog(progId, 1024)
                println("Shader Program Linking Error:\n$log")
                Throwable().printStackTrace()
                return
            }

            GL20.glValidateProgram(progId)

            val validateStatus = GL20.glGetProgrami(progId, GL20.GL_VALIDATE_STATUS)
            if (validateStatus == GL11.GL_FALSE) {
                val log = GL20.glGetProgramInfoLog(progId, 1024)
                println("Shader Program Validation Error:\n$log")
                Throwable().printStackTrace()
                return
            }
        }

        if (deleteShaders) {
            if (fragId != 0) GL20.glDeleteShader(fragId)
            if (vertId != 0) GL20.glDeleteShader(vertId)
        }

        postInit = true
    }

    open fun init() {}

    fun getUniformLoc(name: String) = uniformCache.getOrPut(name) { GL20.glGetUniformLocation(progId, name) }

    fun destroy() {
        if (createProgram) GL20.glDeleteProgram(progId)
        if (!deleteShaders) {
            if (fragId != 0) GL20.glDeleteShader(fragId)
            if (vertId != 0) GL20.glDeleteShader(vertId)
        }
    }

    fun bind() {
        if (!createProgram) throw IllegalStateException("erm no")
        if (!init) build()
        GlState.bindShader(progId)
        if (postInit) {
            postInit = false
            init()
        }
    }

    fun unbind() {
        GlState.bindShader(0)
    }

    companion object {
        @JvmStatic
        protected fun getResource(name: String) = javaClass.getResourceAsStream(name)?.bufferedReader().use { it?.readText() }
    }
}