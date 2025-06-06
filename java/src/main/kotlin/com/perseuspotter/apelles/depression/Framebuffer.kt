package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.state.GlState
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30.*
import java.awt.image.BufferedImage
import java.nio.ByteBuffer

class Framebuffer(var width: Int, var height: Int, val useDepth: Boolean, val useStencil: Boolean, val useDepthTexture: Boolean = false) {
    var textureWidth: Int = width
    var textureHeight: Int = height
    var framebufferObject: Int
    var framebufferTexture: Int
    var depthBuffer: Int
    var color: FloatArray

    init {
        framebufferObject = -1
        framebufferTexture = -1
        depthBuffer = -1
        color = FloatArray(4)
        color[0] = 1.0f
        color[1] = 1.0f
        color[2] = 1.0f
        color[3] = 0.0f
        createAndCheck(width, height)
    }

    fun createAndCheck(width: Int, height: Int) {
        if (framebufferObject >= 0) delete()

        create(width, height)
        checkComplete()
        unbindFramebuffer()
    }

    fun delete() {
        unbindTexture()
        unbindFramebuffer()

        if (depthBuffer > -1) {
            if (useDepthTexture) glDeleteTextures(depthBuffer)
            else glDeleteRenderbuffers(depthBuffer)
            depthBuffer = -1
        }

        if (framebufferTexture > -1) {
            glDeleteTextures(framebufferTexture)
            framebufferTexture = -1
        }

        if (framebufferObject > -1) {
            unbindFramebuffer()
            glDeleteFramebuffers(framebufferObject)
            framebufferObject = -1
        }
    }

    private fun create(width: Int, height: Int) {
        this.width = width
        this.height = height
        textureWidth = width
        textureHeight = height

        framebufferObject = glGenFramebuffers()
        framebufferTexture = glGenTextures()
        if (useDepth || useStencil) depthBuffer = if (useDepthTexture) glGenTextures() else glGenRenderbuffers()

        bindTexture()
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA32F,
            textureWidth,
            textureHeight,
            0,
            GL_RGBA,
            GL_HALF_FLOAT,
            null as ByteBuffer?
        )
        bindFramebuffer()
        glFramebufferTexture2D(
            GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
            framebufferTexture, 0
        )

        if (useDepth || useStencil) {
            if (useDepthTexture) {
                bindDepthTexture()
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
                glTexParameteri(GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL_NONE)
                glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    if (useStencil) GL_DEPTH24_STENCIL8 else GL14.GL_DEPTH_COMPONENT24,
                    textureWidth,
                    textureHeight,
                    0,
                    if (useStencil) GL_DEPTH_STENCIL else GL_DEPTH_COMPONENT,
                    if (useStencil) GL_UNSIGNED_INT_24_8 else GL_UNSIGNED_INT,
                    null as ByteBuffer?
                )
                glFramebufferTexture2D(
                    GL_FRAMEBUFFER,
                    if (useStencil) GL_DEPTH_STENCIL_ATTACHMENT else GL_DEPTH_ATTACHMENT,
                    GL_TEXTURE_2D,
                    depthBuffer,
                    0
                )
            } else {
                glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer)
                glRenderbufferStorage(
                    GL_RENDERBUFFER,
                    if (useStencil) GL_DEPTH24_STENCIL8 else GL14.GL_DEPTH_COMPONENT24,
                    textureWidth,
                    textureHeight
                )
                glFramebufferRenderbuffer(
                    GL_FRAMEBUFFER,
                    if (useStencil) GL_DEPTH_STENCIL_ATTACHMENT else GL_DEPTH_ATTACHMENT,
                    GL_RENDERBUFFER,
                    depthBuffer
                )
            }
        }

        clear()
        unbindTexture()
    }

    fun checkComplete() {
        val i = glCheckFramebufferStatus(GL_FRAMEBUFFER)

        if (i != GL_FRAMEBUFFER_COMPLETE) {
            if (i == GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT) {
                throw RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT")
            } else if (i == GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT) {
                throw RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT")
            } else if (i == GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER) {
                throw RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER")
            } else if (i == GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER) {
                throw RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER")
            } else {
                throw RuntimeException("glCheckFramebufferStatus returned unknown status:$i")
            }
        }
    }

    fun bindTexture() {
        GlState.bindTexture(framebufferTexture)
    }

    fun unbindTexture() {
        GlState.bindTexture(0)
    }

    fun bindDepthTexture() {
        GlState.bindTexture(depthBuffer)
    }

    fun bindFramebuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferObject)
    }

    fun unbindFramebuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun setColor(r: Float, g: Float, b: Float, a: Float) {
        color[0] = r
        color[1] = g
        color[2] = b
        color[3] = a
    }

    fun clear(bit: Int = GL_COLOR_BUFFER_BIT or (if (useDepth) GL_DEPTH_BUFFER_BIT else 0) or (if (useStencil) GL_STENCIL_BUFFER_BIT else 0)) {
        bindFramebuffer()
        glClearColor(color[0], color[1], color[2], color[3])
        if (useDepth) glClearDepth(1.0)
        if (useStencil) glClearStencil(0)
        glClear(bit)
        unbindFramebuffer()
    }

    companion object {
        open class ColorTransformer {
            open fun r(v: Float): Float = v
            open fun g(v: Float): Float = v
            open fun b(v: Float): Float = v
            open fun a(v: Float): Float = v
        }
    }

    fun dumpColor(transformer: ColorTransformer = ColorTransformer()): BufferedImage {
        val buffer = BufferUtils.createFloatBuffer(width * height * 4) // RGBA32F

        glReadBuffer(GL_COLOR_ATTACHMENT0)
        glReadPixels(0, 0, width, height, GL_RGBA, GL_FLOAT, buffer)

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = ((height - 1 - y) * width + x) * 4

                val r = transformer.r(buffer[index + 0]).coerceIn(0f, 1f)
                val g = transformer.g(buffer[index + 1]).coerceIn(0f, 1f)
                val b = transformer.b(buffer[index + 2]).coerceIn(0f, 1f)
                val a = transformer.a(buffer[index + 3]).coerceIn(0f, 1f)

                val ir = (r * 255.0f).toInt()
                val ig = (g * 255.0f).toInt()
                val ib = (b * 255.0f).toInt()
                val ia = (a * 255.0f).toInt()

                val argb = (ia shl 24) or (ir shl 16) or (ig shl 8) or ib
                image.setRGB(x, y, argb)
            }
        }

        return image
    }

    fun dumpDepth(): BufferedImage {
        if (!useDepth) throw IllegalStateException("dumbass")
        val buffer = BufferUtils.createFloatBuffer(width * height)
        glReadPixels(0, 0, width, height, GL_DEPTH_COMPONENT, GL_FLOAT, buffer)

        val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = (height - 1 - y) * width + x
                val depth = buffer[index].coerceIn(0f, 1f)
                val grayscale = (depth * 255).toInt()
                val rgb = (grayscale shl 16) or (grayscale shl 8) or grayscale
                image.setRGB(x, y, rgb)
            }
        }

        return image
    }

    fun dumpStencil(): BufferedImage {
        if (!useStencil) throw IllegalStateException("dumbass")
        val buffer = BufferUtils.createByteBuffer(width * height)
        glReadPixels(0, 0, width, height, GL_STENCIL_INDEX, GL_UNSIGNED_BYTE, buffer)

        val image = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = (height - 1 - y) * width + x
                val grayscale = 255 and buffer[index].toInt()
                val rgb = (grayscale shl 16) or (grayscale shl 8) or grayscale
                image.setRGB(x, y, rgb)
            }
        }

        return image
    }
}