package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.state.GlState
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL20

open class ChromaShader(fragSrc: String?, vertSrc: String?, private val twoD: Boolean, geomSrc: String? = null, deleteShaders: Boolean = true, leech: Shader? = null) : Shader(fragSrc, vertSrc, geomSrc, true, deleteShaders, leech = leech) {
    companion object {
        val instances = mutableListOf<ChromaShader>()

        val CHROMA_3D_BASE = Shader(getResource("/shaders/chroma/chroma_3d.fsh"), getResource("/shaders/chroma/chroma_3d.vsh"), null, false, false)
        val CHROMA_3D_TEX_BASE = Shader(getResource("/shaders/chroma/chroma_3d_tex.fsh"), getResource("/shaders/chroma/chroma_3d_tex.vsh"), null, false, false)
        val CHROMA_2D_BASE = Shader(getResource("/shaders/chroma/chroma_2d.fsh"), getResource("/shaders/chroma/chroma_2d.vsh"), null, false, false)
        val CHROMA_2D_TEX_BASE = Shader(getResource("/shaders/chroma/chroma_2d_tex.fsh"), getResource("/shaders/chroma/chroma_2d_tex.vsh"), null, false, false)

        class ChromaBundle(geomSrc: String?) {
            val CHROMA_3D = ChromaShader(null, null, false, geomSrc, false, CHROMA_3D_BASE)
            val CHROMA_3D_TEX = ChromaShader(null, null, false, geomSrc, false, CHROMA_3D_TEX_BASE)
            val CHROMA_2D = ChromaShader(null, null, true, geomSrc, false, CHROMA_2D_BASE)
            val CHROMA_2D_TEX = ChromaShader(null, null, true, geomSrc, false, CHROMA_2D_TEX_BASE)

            fun get(num: Int, tex: Boolean): ChromaShader {
                return if (tex) (if (num == 1) CHROMA_2D_TEX else CHROMA_3D_TEX) else (if (num == 1) CHROMA_2D else CHROMA_3D)
            }
        }

        var normalBundle: ChromaBundle = ChromaBundle(null)

        fun init() {
            CHROMA_3D_BASE.build()
            CHROMA_3D_TEX_BASE.build()
            CHROMA_2D_BASE.build()
            CHROMA_2D_TEX_BASE.build()
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
        if (twoD) GL20.glUniform1f(getUniformLoc("oneOverDisplayWidth"), 1f / Minecraft.getMinecraft().displayWidth)
    }
}