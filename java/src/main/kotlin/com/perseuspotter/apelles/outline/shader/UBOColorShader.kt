package com.perseuspotter.apelles.outline.shader

import com.perseuspotter.apelles.depression.ChromaShader
import com.perseuspotter.apelles.state.Color
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL31
import kotlin.math.min

abstract class UBOColorShader(fragSrc: String?, vertSrc: String?) : ChromaShader(fragSrc, vertSrc, true) {
    var uboId = -1
    override fun init() {
        bind()
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

    val buf = BufferUtils.createFloatBuffer(256 * 4)
    fun setColors(colors: List<Color>) {
        buf.clear()
        colors.forEach {
            buf.put(it.r)
            buf.put(it.g)
            buf.put(it.b)
            buf.put(it.a)
        }
        buf.flip()
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, buf)
    }

    class ColorBuilder {
        val colors = linkedMapOf<Color, Int>()
        fun getId(col: Color) = min(255, colors.getOrPut(col) { colors.size })
        fun toList(): List<Color> {
            var colList = colors.toList().sortedBy { it.second }.map { it.first }
            if (colList.size > 256) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("Only up to 256 unique colors are supported per frame for each phase and occluded entity outlines."))
                colList = colList.subList(0, 256)
            }
            return colList
        }
    }
}