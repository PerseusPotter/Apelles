package com.perseuspotter.apelles.depression

import com.perseuspotter.apelles.geo.Geometry
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL31

open class RenderShader(
    fragSrc: String?,
    vertSrc: String?,
    protected val lighting: Lighting,
    protected val chroma: Chroma,
    protected val textured: Boolean,
    protected val alphaTest: Boolean,
    deleteShaders: Boolean = true,
    fragLeech: Shader? = null,
    vertLeech: Shader? = null
) : Shader(
    fragSrc?.indexOf('\n')?.let { fragSrc.substring(0, it + 1) + getDefinitions(lighting, chroma, textured, alphaTest) + fragSrc.substring(it + 1) },
    vertSrc?.indexOf('\n')?.let { vertSrc.substring(0, it + 1) + getDefinitions(lighting, chroma, textured, alphaTest) + vertSrc.substring(it + 1) },
    true, deleteShaders, fragLeech, vertLeech
) {
    companion object {
        val instances = mutableListOf<RenderShader>()

        var cRtPt = 0.0
        var cRtT = 0
        var uboDirty = true
        var uboId = -1
        fun markUniformsDirty(pt: Double, t: Int) {
            uboDirty = true
            instances.forEach { it.needUpdateUniforms = true }
            cRtPt = pt
            cRtT = t
        }

        enum class Lighting {
            None, Smooth, Flat
        }

        enum class Chroma {
            None, TwoD, ThreeD
        }

        protected fun getDefinitions(lighting: Lighting, chroma: Chroma, textured: Boolean, alphaTest: Boolean): String {
            val sb = StringBuffer()

            if (lighting != Lighting.None) sb.append("#define LIGHTING\n")
            if (lighting == Lighting.Smooth) sb.append("#define LIGHTING_BLINN_PHONG\n")
            if (lighting == Lighting.Flat) sb.append("#define LIGHTING_FLAT\n")

            if (chroma != Chroma.None) sb.append("#define CHROMA\n")
            if (chroma == Chroma.TwoD) sb.append("#define CHROMA_2D\n")
            if (chroma == Chroma.ThreeD) sb.append("#define CHROMA_3D\n")

            if (textured) sb.append("#define TEXTURED\n")
            if (alphaTest) sb.append("#define ALPHA_TEST\n")

            return sb.toString()
        }

        protected val programs: Array<RenderShader>
        init {
            val fragSrc = getResource("/shaders/render/render.frag")
            val vertSrc = getResource("/shaders/render/render.vert")
            programs = Array(36) {
                val lighting = Lighting.entries[it / 12]
                val chroma = Chroma.entries[(it / 4) % 3]
                val textured = (it and 2) > 0
                val alphaTest = (it and 1) > 0
                RenderShader(fragSrc, vertSrc, lighting, chroma, textured, alphaTest)
            }
        }

        fun get(lighting: Int, chroma: Int, textured: Boolean, alphaTest: Boolean): RenderShader {
            val id = lighting * 12 + chroma * 4 + (if (textured) 2 else 0) + (if (alphaTest) 1 else 0)
            return programs[id]
        }
    }

    init {
        instances.add(this)
    }

    protected var uboIdx = -1

    protected var needUpdateUniforms = false
    override fun bind() {
        super.bind()
        if (lighting != Lighting.None) {
            bindUbo()
            if (uboDirty) {
                GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, Geometry.getLights(), GL15.GL_STATIC_DRAW)
                uboDirty = false
            }
        }
        if (needUpdateUniforms) {
            needUpdateUniforms = false
            updateUniforms(cRtPt, cRtT)
        }
    }

    override fun unbindCleanup() {
        unbindUbo()
    }

    override fun init() {
        if (textured) GL20.glUniform1i(getUniformLoc("uTex"), 0)
        if (alphaTest) GL20.glUniform1f(getUniformLoc("uAlphaThresh"), 0.5f)
        if (lighting != Lighting.None && uboId < 0) {
            uboId = GL15.glGenBuffers()
            GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 1, uboId)
        }
    }

    protected fun bindUbo() {
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uboId)
        if (uboIdx < 0) uboIdx = GL31.glGetUniformBlockIndex(progId, "lightsUbo")
        GL31.glUniformBlockBinding(progId, uboIdx, 1)
    }

    protected fun unbindUbo() {
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0)
    }

    protected open fun updateUniforms(pt: Double, t: Int) {
        if (lighting == Lighting.None) GL20.glUniformMatrix4(getUniformLoc("uModelViewProj"), false, Geometry.getMVPMatrix())
        else {
            GL20.glUniformMatrix4(getUniformLoc("uModelView"), false, Geometry.getModelViewMatrix())
            GL20.glUniformMatrix4(getUniformLoc("uProj"), false, Geometry.getProjectionMatrix())
            GL20.glUniformMatrix3(getUniformLoc("uNorm"), false, Geometry.getNormalMatrix())
        }
        if (chroma != Chroma.None) GL20.glUniform1f(getUniformLoc("uTimeOffset"), (t + pt).toFloat() / 5f)
        if (chroma == Chroma.TwoD) GL20.glUniform1f(getUniformLoc("uOneOverDisplayWidth"), 1f / Minecraft.getMinecraft().displayWidth)
        if (chroma == Chroma.ThreeD) GL20.glUniform3f(
            getUniformLoc("uViewPos"),
            Geometry.getRenderX().toFloat(),
            Geometry.getRenderY().toFloat(),
            Geometry.getRenderZ().toFloat()
        )
    }
}