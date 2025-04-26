package com.perseuspotter.apelles.depression

import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL20

open class ChromaShader(fragSrc: String?, vertSrc: String?, private val twoD: Boolean) : Shader(fragSrc, vertSrc) {
    companion object {
        val CHROMA_3D = ChromaShader(getResource("/shaders/chroma_3d.fsh"), getResource("/shaders/chroma_3d.vsh"), false)
        val CHROMA_3D_TEX = ChromaShader(getResource("/shaders/chroma_3d_tex.fsh"), getResource("/shaders/chroma_3d_tex.vsh"), false)
        val CHROMA_2D = ChromaShader(getResource("/shaders/chroma_2d.fsh"), getResource("/shaders/chroma_2d.vsh"), true)
        val CHROMA_2D_TEX = ChromaShader(getResource("/shaders/chroma_2d_tex.fsh"), getResource("/shaders/chroma_2d_tex.vsh"), true)
        fun get(num: Int, tex: Boolean) = if (tex) (if (num == 1) CHROMA_2D_TEX else CHROMA_3D_TEX) else (if (num == 1) CHROMA_2D else CHROMA_3D)
    }

    open fun updateUniforms(pt: Double, t: Int) {
        GL20.glUniform1f(getUniformLoc("timeOffset"), (t + pt).toFloat() / (if (twoD) 20f else 5f))
        if (twoD) GL20.glUniform1f(getUniformLoc("oneOverDisplayWidth"), 1f / Minecraft.getMinecraft().displayWidth)
    }
}