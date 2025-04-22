package com.perseuspotter.apelles.depression

import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL20

open class ChromaShader(fragSrc: String?, vertSrc: String?) : Shader(fragSrc, vertSrc) {
    companion object {
        val CHROMA_3D = ChromaShader(getResource("/shaders/chroma_3d.fsh"), getResource("/shaders/chroma_3d.vsh"))
        val CHROMA_3D_TEX = ChromaShader(getResource("/shaders/chroma_3d_tex.fsh"), getResource("/shaders/chroma_3d_tex.vsh"))
    }

    open fun updateUniforms(pt: Double) {
        GL20.glUniform1f(getUniformLoc("timeOffset"), (Minecraft.getMinecraft().theWorld.worldTime + pt).toFloat())
    }
}