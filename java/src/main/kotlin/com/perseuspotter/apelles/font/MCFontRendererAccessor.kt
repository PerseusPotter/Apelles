package com.perseuspotter.apelles.font

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation

object MCFontRendererAccessor {
    val fr = Minecraft.getMinecraft().fontRendererObj

    val charWidth = fr::class.java.getDeclaredField("field_78286_d").also { it.isAccessible = true }.get(fr) as IntArray
    val glyphWidth = fr::class.java.getDeclaredField("field_78287_e").also { it.isAccessible = true }.get(fr) as ByteArray
    val locationFontTexture = fr::class.java.getDeclaredField("field_111273_g").also { it.isAccessible = true }.get(fr) as ResourceLocation
    private val getUnicodePageLocationF = fr::class.java.getDeclaredMethod("func_111271_a", Int::class.javaPrimitiveType).also { it.isAccessible = true }
    fun getUnicodePageLocation(page: Int) = getUnicodePageLocationF.invoke(fr, page) as ResourceLocation
}