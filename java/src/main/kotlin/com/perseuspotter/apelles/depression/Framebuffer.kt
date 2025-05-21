package com.perseuspotter.apelles.depression

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*
import java.awt.image.BufferedImage
import java.lang.IllegalStateException
import java.nio.ByteBuffer
import kotlin.math.sqrt


class Framebuffer(var width: Int, var height: Int, val useDepth: Boolean, val useStencil: Boolean) {
    var textureWidth: Int = width
    var textureHeight: Int = height
    var framebufferObject: Int
    var framebufferTexture: Int
    var depthBuffer: Int
    var color: FloatArray
    var filter: Int = 0

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
            glDeleteRenderbuffers(depthBuffer)
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

        if (useDepth || useStencil) depthBuffer = glGenRenderbuffers()

        setTexFilter(GL_NEAREST)
        bindTexture()
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
            this.framebufferTexture, 0
        )

        if (useDepth || useStencil) {
            glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer)
            glRenderbufferStorage(
                GL_RENDERBUFFER, if (useStencil) GL_DEPTH24_STENCIL8 else GL14.GL_DEPTH_COMPONENT24,
                textureWidth,
                textureHeight
            )
            glFramebufferRenderbuffer(
                GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER,
                depthBuffer
            )
        }

        clear()
        unbindTexture()
    }

    fun setTexFilter(filter: Int) {
        this.filter = filter
        bindTexture()
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP)
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
        glBindTexture(GL_TEXTURE_2D, framebufferTexture)
    }

    fun unbindTexture() {
        glBindTexture(GL_TEXTURE_2D, 0)
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

    fun dumpColor(): BufferedImage {
        val buffer = BufferUtils.createFloatBuffer(width * height * 4) // RGBA32F

        glReadBuffer(GL_COLOR_ATTACHMENT0)
        glReadPixels(0, 0, width, height, GL_RGBA, GL_FLOAT, buffer)

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = ((height - 1 - y) * width + x) * 4

                val r = (sqrt(buffer[index + 0]) / 255f).coerceIn(0f, 1f)
                val g = (buffer[index + 1] / 1000f).coerceIn(0f, 1f)
                val b = (buffer[index + 2] / 1000f).coerceIn(0f, 1f)
                val a = (buffer[index + 3]).coerceIn(0f, 1f)

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
                val depth = 255 and buffer[index].toInt()
                val grayscale = depth / 255
                val rgb = (grayscale shl 16) or (grayscale shl 8) or grayscale
                image.setRGB(x, y, rgb)
            }
        }

        return image
    }
}