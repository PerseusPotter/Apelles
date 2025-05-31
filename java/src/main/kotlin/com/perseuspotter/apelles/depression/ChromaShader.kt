package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.geo.Geometry
import com.perseuspotter.apelles.state.GlState
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL20

open class ChromaShader(fragSrc: String?, vertSrc: String?, private val twoD: Boolean, deleteShaders: Boolean = true, fragLeech: Shader? = null, vertLeech: Shader? = null) : Shader(fragSrc, vertSrc, true, deleteShaders, fragLeech, vertLeech) {
    companion object {
        val instances = mutableListOf<ChromaShader>()

        val CHROMA_3D = ChromaShader(getResource("/shaders/chroma/chroma_3d.fsh"), getResource("/shaders/chroma/chroma_3d.vsh"), false)
        val CHROMA_3D_TEX = ChromaShader(getResource("/shaders/chroma/chroma_3d_tex.fsh"), getResource("/shaders/chroma/chroma_3d_tex.vsh"), false)
        val CHROMA_2D = ChromaShader(getResource("/shaders/chroma/chroma_2d.fsh"), getResource("/shaders/chroma/chroma_2d.vsh"), true)
        val CHROMA_2D_TEX = ChromaShader(getResource("/shaders/chroma/chroma_2d_tex.fsh"), getResource("/shaders/chroma/chroma_2d_tex.vsh"), true)

        fun get(is2D: Boolean, tex: Boolean): ChromaShader {
            return if (tex) (if (is2D) CHROMA_2D_TEX else CHROMA_3D_TEX) else (if (is2D) CHROMA_2D else CHROMA_3D)
        }

        fun updateUniforms(pt: Double, t: Int) {
            instances.forEach {
                it.bind()
                it.updateUniforms(pt, t)
            }
            GlState.bindShader(0)
        }
    }

    init {
        instances.add(this)
    }

    protected open fun updateUniforms(pt: Double, t: Int) {
        GL20.glUniform1f(getUniformLoc("timeOffset"), (t + pt).toFloat() / 5f)
        if (twoD) {
            GL20.glUniform1f(getUniformLoc("oneOverDisplayWidth"), 1f / Minecraft.getMinecraft().displayWidth)
        } else {
            GL20.glUniform3f(getUniformLoc("playerPos"),
                Geometry.getRenderX().toFloat(), Geometry.getRenderY().toFloat(), Geometry.getRenderZ().toFloat()
            )
        }
    }
}