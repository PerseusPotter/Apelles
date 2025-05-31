package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.state.GlState
import net.minecraft.client.renderer.ActiveRenderInfo
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.nio.FloatBuffer

open class Shader(val fragSrc: String?, val vertSrc: String?, val createProgram: Boolean = true, val deleteShaders: Boolean = true, var fragLeech: Shader? = null, var vertLeech: Shader? = null) {
    var init = false
    var progId = 0
    val uniformCache = mutableMapOf<String, Int>()
    var fragId = 0
    var vertId = 0

    fun build() {
        init = true

        if (fragLeech != null) fragId = fragLeech!!.fragId
        if (vertLeech != null) vertId = vertLeech!!.vertId

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

        GlState.bindShader(progId)
        init()
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
    }

    fun unbind() {
        GlState.bindShader(0)
    }

    companion object {
        @JvmStatic
        protected fun getResource(name: String) = javaClass.getResourceAsStream(name)?.bufferedReader().use { it?.readText() }

        val viewProjMatrix = BufferUtils.createFloatBuffer(16)
        val MODELVIEW: FloatBuffer = ActiveRenderInfo::class.java.getDeclaredField("field_178812_b").also { it.isAccessible = true }.get(null) as FloatBuffer
        val PROJECTION: FloatBuffer = ActiveRenderInfo::class.java.getDeclaredField("field_178813_c").also { it.isAccessible = true }.get(null) as FloatBuffer
        fun updateInfo() {
            val view = FloatArray(16)
            val proj = FloatArray(16)
            MODELVIEW.get(view).rewind()
            PROJECTION.get(proj).rewind()

            viewProjMatrix.clear()
            viewProjMatrix.put(proj[0] * view[0] + proj[4] * view[1] + proj[8] * view[2] + proj[12] * view[3])
            viewProjMatrix.put(proj[1] * view[0] + proj[5] * view[1] + proj[9] * view[2] + proj[13] * view[3])
            viewProjMatrix.put(proj[2] * view[0] + proj[6] * view[1] + proj[10] * view[2] + proj[14] * view[3])
            viewProjMatrix.put(proj[3] * view[0] + proj[7] * view[1] + proj[11] * view[2] + proj[15] * view[3])

            viewProjMatrix.put(proj[0] * view[4] + proj[4] * view[5] + proj[8] * view[6] + proj[12] * view[7])
            viewProjMatrix.put(proj[1] * view[4] + proj[5] * view[5] + proj[9] * view[6] + proj[13] * view[7])
            viewProjMatrix.put(proj[2] * view[4] + proj[6] * view[5] + proj[10] * view[6] + proj[14] * view[7])
            viewProjMatrix.put(proj[3] * view[4] + proj[7] * view[5] + proj[11] * view[6] + proj[15] * view[7])

            viewProjMatrix.put(proj[0] * view[8] + proj[4] * view[9] + proj[8] * view[10] + proj[12] * view[11])
            viewProjMatrix.put(proj[1] * view[8] + proj[5] * view[9] + proj[9] * view[10] + proj[13] * view[11])
            viewProjMatrix.put(proj[2] * view[8] + proj[6] * view[9] + proj[10] * view[10] + proj[14] * view[11])
            viewProjMatrix.put(proj[3] * view[8] + proj[7] * view[9] + proj[11] * view[10] + proj[15] * view[11])

            viewProjMatrix.put(proj[0] * view[12] + proj[4] * view[13] + proj[8] * view[14] + proj[12] * view[15])
            viewProjMatrix.put(proj[1] * view[12] + proj[5] * view[13] + proj[9] * view[14] + proj[13] * view[15])
            viewProjMatrix.put(proj[2] * view[12] + proj[6] * view[13] + proj[10] * view[14] + proj[14] * view[15])
            viewProjMatrix.put(proj[3] * view[12] + proj[7] * view[13] + proj[11] * view[14] + proj[15] * view[15])
            viewProjMatrix.flip()
        }
    }
}