package com.perseuspotter.apelles.state

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20

object GlState {
    fun push() {
        glPushAttrib(GL_ALL_ATTRIB_BITS)
    }

    fun pop() {
        glPopAttrib()
    }

    private var prevLw: Float? = null
    fun lineWidth(lw: Float) {
        if (lw != prevLw) {
            prevLw = lw
            glLineWidth(lw)
        }
    }
    private var prevSmooth: Boolean? = null
    fun lineSmooth(smooth: Boolean) {
        if (smooth != prevSmooth) {
            prevSmooth = smooth
            if (smooth) glEnable(GL_LINE_SMOOTH)
            else glDisable(GL_LINE_SMOOTH)
        }
    }
    private var boundTex: ResourceLocation? = null
    fun bindTexture(tex: ResourceLocation) {
        if (tex != boundTex) {
            boundTex = tex
            Minecraft.getMinecraft().textureManager.bindTexture(tex)
        }
    }
    private var cr: Float? = null
    private var cg: Float? = null
    private var cb: Float? = null
    private var ca: Float? = null
    fun color(r: Float, g: Float, b: Float, a: Float) {
        if (cr != r || cg != g || cb != b || ca != a) {
            glColor4f(r, g, b, a)
            cr = r
            cg = g
            cb = b
            ca = a
        }
    }
    private var depthTest: Boolean? = null
    fun setDepthTest(depth: Boolean) {
        if (depthTest != depth) {
            if (depth) glDepthFunc(GL_LEQUAL)
            else glDepthFunc(GL_ALWAYS)
            depthTest = depth
        }
    }
    private var lighting: Int? = null
    fun setLighting(light: Int) {
        if (lighting != light) {
            if (light > 0 && (lighting == null || lighting == 0)) glEnable(GL_LIGHTING)
            when (light) {
                0 -> glDisable(GL_LIGHTING)
                1 -> glShadeModel(GL_SMOOTH)
                2 -> glShadeModel(GL_FLAT)
            }
            lighting = light
        }
    }
    fun isLightingEnabled() = lighting!! > 0
    private var boundShader: Int? = null
    fun bindShader(shader: Int) {
        if (boundShader != shader) {
            GL20.glUseProgram(shader)
            boundShader = shader
        }
    }
    private var colorArray: Boolean? = null
    fun setColorArray(enabled: Boolean) {
        if (enabled != colorArray) {
            if (enabled) glEnable(GL_COLOR_ARRAY)
            else glDisable(GL_COLOR_ARRAY)
            colorArray = enabled
        }
    }
    private var normalArray: Boolean? = null
    fun setNormalArray(enabled: Boolean) {
        if (enabled != normalArray) {
            if (enabled) glEnable(GL_NORMAL_ARRAY)
            else glDisable(GL_NORMAL_ARRAY)
            normalArray = enabled
        }
    }
    private var texArray: Boolean? = null
    fun setTexArray(enabled: Boolean) {
        if (enabled != texArray) {
            if (enabled) glEnable(GL_TEXTURE_COORD_ARRAY)
            else glDisable(GL_TEXTURE_COORD_ARRAY)
            texArray = enabled
        }
    }

    fun reset() {
        prevLw = null
        prevSmooth = null
        boundTex = null
        cr = null
        cg = null
        cb = null
        ca = null
        depthTest = null
        lighting = null
        boundShader = null
    }
}