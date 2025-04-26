package com.perseuspotter.apelles.outline

import com.perseuspotter.apelles.depression.Framebuffer
import com.perseuspotter.apelles.geo.Frustum
import com.perseuspotter.apelles.outline.outliner.RenderOutliner
import com.perseuspotter.apelles.outline.shader.InitPass
import com.perseuspotter.apelles.outline.shader.JFAPass
import com.perseuspotter.apelles.outline.shader.JFARender
import com.perseuspotter.apelles.state.Color
import com.perseuspotter.apelles.state.GlState
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.entity.Entity
import net.minecraft.util.ChatComponentText
import org.lwjgl.opengl.*
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.min

object EntityOutlineRenderer {
    val outlined = WeakHashMap<Entity, OutlineState>()
    fun getOutlineState(e: Entity): OutlineState = outlined.getOrPut(e) { OutlineState(e) }
    val outliners = linkedSetOf<RenderOutliner>()
    var fb1: Framebuffer? = null
    var fb2: Framebuffer? = null

    private fun createFB(): Framebuffer {
        val main = Minecraft.getMinecraft().framebuffer
        val fb = Framebuffer(main.framebufferTextureWidth, main.framebufferTextureHeight, true)
        fb.setColor(0.0f, 0.0f, 0.0f, 0.0f)
        return fb
    }

    var CAN_OUTLINE = false
    private var checked = false
    fun renderOutlines(pt: Double) {
        if (!checked) {
            val cap = GLContext.getCapabilities()
            CAN_OUTLINE = cap.GL_ARB_fragment_shader && cap.GL_ARB_framebuffer_object && cap.GL_ARB_uniform_buffer_object && cap.GL_ARB_shading_language_420pack
            checked = true
            // GL11.glEnable(GL43.GL_DEBUG_OUTPUT)
            // GL43.glDebugMessageCallback(KHRDebugCallback { source: Int, type: Int, id: Int, severity: Int, message: String? ->
            //     if (severity == GL43.GL_DEBUG_SEVERITY_NOTIFICATION || id == 131154) return@KHRDebugCallback
            //     println("ye fucked up")
            //     println("source: $source type: $type id: $id severity $severity message: $message")
            // })
        }
        if (!CAN_OUTLINE) return
        val phase = mutableListOf<OutlineState>()
        val occluded = mutableListOf<OutlineState>()
        var isThereShit = false
        val prof = Minecraft.getMinecraft().mcProfiler
        prof.startSection("testEntities")
        Minecraft.getMinecraft().theWorld.loadedEntityList.forEach { e ->
            if (e.isInvisible) return@forEach
            // good enough
            if (!Frustum.test(e.posX, e.posY, e.posZ)) return@forEach
            outliners.forEach { if (it.registered) it.test(e) }
        }
        outlined.forEach { (e, s) ->
            if (e.isDead) return@forEach
            if (e.isInvisible) return@forEach
            if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && e is EntityPlayerSP) return@forEach
            if (!s.doOutline()) return@forEach
            if (s.getColor().a == 0f) return@forEach
            isThereShit = true
            (if (s.getPhase()) phase else occluded).add(s)
        }
        prof.endSection()
        if (!isThereShit) return

        prof.startSection("setup")
        if (fb1 == null) {
            fb1 = createFB()
            fb2 = createFB()
        }
        val width = Minecraft.getMinecraft().displayWidth
        val height = Minecraft.getMinecraft().displayHeight
        if (fb1!!.width != width || fb1!!.height != height) {
            fb1!!.createAndCheck(width, height)
            fb2!!.createAndCheck(width, height)
        }

        GlState.setLighting(0)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        Minecraft.getMinecraft().renderManager.setRenderOutlines(true)

        fb1!!.clear()
        val mainFb = Minecraft.getMinecraft().framebuffer
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainFb.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fb1!!.framebufferObject)
        GL30.glBlitFramebuffer(0, 0, mainFb.framebufferWidth, mainFb.framebufferHeight, 0, 0, fb1!!.width, fb1!!.height, GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST)

        prof.endStartSection("phase")
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        doOutline(pt, phase, 0)

        prof.endStartSection("occluded")
        fb1!!.clear(GL11.GL_COLOR_BUFFER_BIT)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GlState.setDepthTest(true)
        doOutline(pt, occluded, 1)

        GlState.bindShader(0)
        Minecraft.getMinecraft().renderManager.setRenderOutlines(false)
        Minecraft.getMinecraft().framebuffer.bindFramebuffer(false)
        outliners.forEach { it.clear() }
        dump = false
        prof.endSection()
    }

    @JvmField
    var dump = false
    private fun ceilLog2(x: Int): Int = 32 - (x - 1).coerceAtLeast(0).countLeadingZeroBits()
    private fun doOutline(pt: Double, ents: List<OutlineState>, pass: Int) {
        if (ents.isEmpty()) return

        val rm = Minecraft.getMinecraft().renderManager
        val mainFb = Minecraft.getMinecraft().framebuffer

        OpenGlHelper.glBlendFunc(1, 0, 1, 0)
        GL11.glDisable(GL11.GL_BLEND)

        fb1!!.bindFramebuffer()
        InitPass.bind()
        InitPass.setSize(fb1!!.textureWidth, fb1!!.textureHeight)
        var maxW = 0
        val colors = linkedMapOf<Color, Int>()
        var prevCol = -1
        if (dump) {
            val depthImage = fb1!!.dumpDepth()
            ImageIO.write(depthImage, "png", File("./depthBufferPre$pass.png"))
            println("outlining entities pass $pass: ${ents.joinToString(" ") { it.entity.get()!!.entityId.toString() }}")
        }
        ents.groupBy { it.getWidth() }.forEach { (w, e) ->
            InitPass.setWidth(w)
            // "fixed"
            val w2 = if (w < 0) -16 * w else w
            if (w2 > maxW) maxW = w2
            e.forEach {
                val col = it.getColor()
                val id = min(255, colors.getOrPut(col) { colors.size })
                if (id != prevCol) {
                    InitPass.setColorId(id)
                    prevCol = id
                }
                rm.renderEntityStatic(it.entity.get(), pt.toFloat(), false)
            }
        }
        GlState.reset()
        if (dump) {
            fb1!!.bindFramebuffer()
            val colorImage = fb1!!.dumpColor()
            val depthImage = fb1!!.dumpDepth()
            ImageIO.write(colorImage, "png", File("./colorBufferInit$pass.png"))
            ImageIO.write(depthImage, "png", File("./depthBufferInit$pass.png"))
            println("colors: ${colors.toList().joinToString(" ") { it.first.toString() }}")
        }
        GlState.setDepthTest(false)

        JFAPass.bind()
        JFAPass.setSize(fb1!!.textureWidth, fb1!!.textureHeight)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        var f = true
        var iter = 0
        fun doIter(gap: Int) {
            JFAPass.setGap(gap)
            val fb = if (f) fb1 else fb2
            val pp = if (f) fb2 else fb1
            f = !f
            fb!!.bindTexture()
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb.framebufferObject)
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, pp!!.framebufferObject)
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
            if (dump) {
                pp.bindFramebuffer()
                val colorImage = pp.dumpColor()
                ImageIO.write(colorImage, "png", File("./colorBufferPass$pass$iter.png"))
            }
            iter++
        }
        for (i in ceilLog2(maxW) - 1 downTo 0) {
            doIter(1 shl i)
        }
        doIter(2)
        doIter(1)
        fb1!!.unbindTexture()

        GL11.glEnable(GL11.GL_BLEND)
        OpenGlHelper.glBlendFunc(770, 771, 1, 771)
        JFARender.bind()
        JFARender.updateUniforms(pt)
        JFARender.bindUbo()
        var colList = colors.toList().sortedBy { it.second }.map { it.first }
        if (colList.size > 256) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("Only up to 256 unique colors are supported per frame for each phase and occluded entity outlines."))
            colList = colList.subList(0, 256)
        }
        JFARender.setColors(colList)
        (if (f) fb1 else fb2)!!.bindTexture()
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, (if (f) fb1 else fb2)!!.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, mainFb.framebufferObject)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
        JFARender.unbindUbo()
    }
}