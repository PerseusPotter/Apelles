package com.perseuspotter.apelles.outline.shader

import com.perseuspotter.apelles.depression.ChromaShader
import com.perseuspotter.apelles.state.Color
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL31

object JFARender : ChromaShader(getResource("/shaders/jfaRender.frag"), getResource("/shaders/jfaRender.vert"), true) {
    var uboId = -1
    override fun init() {
        this.bind()
        val b = BufferUtils.createIntBuffer(1)
        GL15.glGenBuffers(b)
        uboId = b[0]

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uboId)
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, 256L * Float.SIZE_BYTES, GL15.GL_STREAM_DRAW)
        GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, uboId)
        GL31.glUniformBlockBinding(progId, 0, GL31.glGetUniformBlockIndex(progId, "colorsUbo"))
        unbindUbo()
    }

    fun bindUbo() {
        GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, uboId)
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uboId)
    }

    fun unbindUbo() {
        GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, 0)
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0)
    }

    val buff = BufferUtils.createFloatBuffer(256)
    fun setColors(colors: List<Color>) {
        buff.clear()
        colors.forEach {
            buff.put(it.r)
            buff.put(it.g)
            buff.put(it.b)
            buff.put(it.a)
        }
        buff.flip()
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, buff)
    }
}