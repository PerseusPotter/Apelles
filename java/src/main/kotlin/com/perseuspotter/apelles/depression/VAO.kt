package com.perseuspotter.apelles.depression

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer
import java.nio.IntBuffer

class VAO(vertexCount: Int, indicesCount: Int, val hasC: Boolean, val hasN: Boolean, val hasT: Boolean, val mode: Int) {
    var vaoId: Int = -1
    var vboId: Int = -1
    var iboId: Int = -1
    val MAX_VERTEX_COUNT = vertexCount * 3 / 2
    val MAX_INDEX_COUNT = indicesCount * 3 / 2
    val VERTEX_SIZE = 3 + (if (hasC) 4 else 0) + (if (hasN) 3 else 0) + (if (hasT) 2 else 0)
    val bufP: FloatBuffer = BufferUtils.createFloatBuffer(MAX_VERTEX_COUNT * VERTEX_SIZE)
    val bufI: IntBuffer = BufferUtils.createIntBuffer(MAX_INDEX_COUNT)
    var vertCount = 0

    fun reset() {
        bufP.clear()
        bufI.clear()
        vertCount = 0
    }

    fun putP(x: Float, y: Float, z: Float) {
        bufP.put(x)
        bufP.put(y)
        bufP.put(z)
        vertCount++
    }
    fun putC(r: Float, g: Float, b: Float, a: Float) {
        bufP.put(r)
        bufP.put(g)
        bufP.put(b)
        bufP.put(a)
    }
    fun putN(x: Float, y: Float, z: Float) {
        bufP.put(x)
        bufP.put(y)
        bufP.put(z)
    }
    fun putT(u: Float, v: Float) {
        bufP.put(u)
        bufP.put(v)
    }
    fun putI(i: Int) {
        bufI.put(i)
    }

    private var built = false
    private fun build() {
        built = true

        vaoId = GL30.glGenVertexArrays()
        vboId = GL15.glGenBuffers()
        iboId = GL15.glGenBuffers()

        GL30.glBindVertexArray(vaoId)

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId)

        var idx = 0
        var off = 0L
        GL20.glVertexAttribPointer(idx, 3, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, off * 4L)
        GL20.glEnableVertexAttribArray(idx++)
        off += 3L

        if (hasC) {
            GL20.glVertexAttribPointer(idx, 4, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, off * 4L)
            GL20.glEnableVertexAttribArray(idx++)
            off += 4L
        }

        if (hasN) {
            // normalization done in Geometry
            GL20.glVertexAttribPointer(idx, 3, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, off * 4L)
            GL20.glEnableVertexAttribArray(idx++)
            off += 3L
        }

        if (hasT) {
            GL20.glVertexAttribPointer(idx, 2, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, off * 4L)
            GL20.glEnableVertexAttribArray(idx++)
            off += 2L
        }
    }

    fun updateAndDraw() {
        if (bufI.position() == 0) return
        if (!built) build()
        else {
            GL30.glBindVertexArray(vaoId)
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
        }

        bufP.flip()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufP, GL15.GL_STREAM_DRAW)

        val drawCount = bufI.position()
        bufI.flip()
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, bufI, GL15.GL_STREAM_DRAW)

        GL11.glDrawElements(mode, drawCount, GL11.GL_UNSIGNED_INT, 0)

        GL30.glBindVertexArray(0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }

    fun destroy() {
        GL30.glDeleteVertexArrays(vaoId)
        GL15.glDeleteBuffers(vboId)
        GL15.glDeleteBuffers(iboId)
    }
}