package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.geo.Geometry
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import java.nio.FloatBuffer
import java.nio.IntBuffer

class VAO(val vertexCount: Int, val indicesCount: Int, val hasC: Boolean, val hasN: Boolean, val hasT: Boolean, val mode: Int) {
    // var vaoId: Int = -1
    var vboPId: Int = -1
    var iboId: Int = -1
    val MAX_VERTEX_COUNT = vertexCount * 3 / 2
    val MAX_INDEX_COUNT = indicesCount * 3 / 2
    val VERTEX_SIZE = 3 + (if (hasC) 4 else 0) + (if (hasN) 3 else 0) + (if (hasT) 2 else 0)
    val bufP: FloatBuffer = BufferUtils.createFloatBuffer(MAX_VERTEX_COUNT * VERTEX_SIZE)
    val bufI: IntBuffer = BufferUtils.createIntBuffer(MAX_INDEX_COUNT)
    var drawCount = 0
    var vertCount = 0
    var hasMeaningfulData = false

    fun reset() {
        bufP.clear()
        bufI.clear()
        drawCount = 0
        vertCount = 0
        hasMeaningfulData = false
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
        if (i == Geometry.PRIMITIVE_RESTART_INDEX && !hasMeaningfulData) return
        bufI.put(i)
        hasMeaningfulData = true
    }

    private var built = false
    private fun build() {
        built = true

        val b = BufferUtils.createIntBuffer(1)

        GL15.glGenBuffers(b)
        vboPId = b[0]

        GL15.glGenBuffers(b)
        iboId = b[0]

        /*
        vaoId = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vaoId)

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboPId)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)
        GL20.glEnableVertexAttribArray(0)

        if (hasC) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboCId)
            GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0)
            GL20.glEnableVertexAttribArray(1)
        } // else GL20.glDisableVertexAttribArray(1)

        if (hasN) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboNId)
            GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0)
            GL20.glEnableVertexAttribArray(2)
        } // else GL20.glDisableVertexAttribArray(2)

        if (hasT) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTId)
            GL20.glVertexAttribPointer(3, 2, GL11.GL_FLOAT, false, 0, 0)
            GL20.glEnableVertexAttribArray(3)
        } // else GL20.glDisableVertexAttribArray(3)

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId)

        GL30.glBindVertexArray(0)
        */
    }

    fun update() {
        if (!hasMeaningfulData) return
        if (!built) build()

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboPId)
        bufP.flip()
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufP, GL15.GL_STREAM_DRAW)

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId)
        drawCount = bufI.position()
        bufI.flip()
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, bufI, GL15.GL_STREAM_DRAW)
    }

    fun draw() {
        if (!hasMeaningfulData) return
        // VAOShader.get(hasC, hasN, hasT).bind()
        // GL30.glBindVertexArray(vaoId)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboPId)
        GL11.glVertexPointer(
            3,
            GL11.GL_FLOAT,
            VERTEX_SIZE * Float.SIZE_BYTES,
            0L
        )
        if (hasC) GL11.glColorPointer(
            4,
            GL11.GL_FLOAT,
            VERTEX_SIZE * Float.SIZE_BYTES,
            3L * Float.SIZE_BYTES
        )
        if (hasN) GL11.glNormalPointer(
            GL11.GL_FLOAT,
            VERTEX_SIZE * Float.SIZE_BYTES,
            (3L + (if (hasC) 4L else 0L)) * Float.SIZE_BYTES
        )
        if (hasT) GL11.glTexCoordPointer(
            2,
            GL11.GL_FLOAT,
            VERTEX_SIZE * Float.SIZE_BYTES,
            (VERTEX_SIZE - 2L) * Float.SIZE_BYTES
        )
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId)
        GL11.glDrawElements(mode, drawCount, GL11.GL_UNSIGNED_INT, 0)
    }

    fun destroy() {
        GL15.glDeleteBuffers(vboPId)
        GL15.glDeleteBuffers(iboId)
        // GL30.glDeleteVertexArrays(vaoId)
    }
}