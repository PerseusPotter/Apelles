package com.perseuspotter.apelles.font

import com.perseuspotter.apelles.state.Color
import net.minecraft.util.ResourceLocation
import kotlin.math.max
import kotlin.math.min

class StringParser(key: StringKey) {
    private val decoratorsList = mutableListOf<Decorator>()
    private val charsList = mutableListOf<CharacterRenderInfoWrapped>()
    private val cumWidthList = mutableListOf<Int>()
    val decorators: Array<Decorator>
    val chars: Array<CharacterRenderInfoWrapped>
    val cumWidth: Array<Int>
    val charCount = mutableMapOf<ResourceLocation, Int>()

    fun getWidthLowerBound(w: Double): Int {
        var l = 0
        var r = cumWidth.size
        while (l < r) {
            val m = (l + r) shr 1
            if (cumWidth[m] < w) l = m + 1
            else r = m
        }
        return min(l, cumWidth.size - 1)
    }
    fun getWidthUpperBound(w: Double): Int {
        var l = 0
        var r = cumWidth.size - 1
        while (l <= r) {
            val m = (l + r) shr 1
            if (cumWidth[m] <= w) l = m + 1
            else r = m - 1
        }
        return max(l - 1, 0)
    }
    fun getWidth() = cumWidth.lastOrNull() ?: 0
    private fun getLength() = cumWidthList.lastOrNull() ?: 0
    fun getXOff(i: Int) = if (i <= 0) 0 else cumWidthList[i - 1]

    private var obfu = false
    private var bold = false
    private var ital = false
    private var strike = -1
    private var under = -1
    private var col = '\u0000'

    private fun reset() {
        obfu = false
        bold = false
        ital = false
        if (strike >= 0) {
            if (strike < getLength()) decoratorsList.add(Decorator(strike, getLength(), CHAR_HEIGHT * 0.5f, 1f, col))
            strike = -1
        }
        if (under >= 0) {
            if (under < getLength()) decoratorsList.add(Decorator(under, getLength(), CHAR_HEIGHT, 1f, col))
            under = -1
        }
        col = '\u0000'
    }

    private fun setColor(co: Char) {
        if (strike >= 0 && strike < getLength()) {
            if (strike < getLength()) decoratorsList.add(Decorator(strike, getLength(), CHAR_HEIGHT * 0.5f, 1f, col))
            strike = getLength()
        }
        if (under >= 0 && under < getLength()) {
            if (under < getLength()) decoratorsList.add(Decorator(under, getLength(), CHAR_HEIGHT, 1f, col))
            under = getLength()
        }
        col = co
    }

    private fun _addChar(info: CharacterRenderInfoWrapped) {
        charsList.add(info)
        val rl = info.getCharInfo().rl
        charCount[rl] = 1 + (charCount.getOrDefault(rl, 10))
    }

    private fun emit(c: Char) {
        val info = FONT.getInfo(c)
        if (obfu) {
            _addChar(CharacterRenderInfoObfuscated(info, getLength(), 0, col, ital))
            if (bold) _addChar(CharacterRenderInfoObfuscated(info, getLength() + 1, 0, col, ital))
        } else {
            _addChar(CharacterRenderInfo(info, getLength(), 0, col, ital))
            if (bold) _addChar(CharacterRenderInfo(info, getLength() + 1, 0, col, ital))
        }
        cumWidthList.add(getLength() + info.w)
        if (bold) cumWidthList.add(getLength() + 1)
    }

    init {
        val str = key.string
        val parseMode = key.parseMode
        val doSpecial = parseMode and 1 == 1
        var i = 0
        while (i < str.length) {
            val c = str[i]
            i++
            val skip = if (parseMode == 0) false else {
                var isSpecial = i < str.length && ((c == '§' && parseMode >= 1) || (c == '&' && parseMode >= 3))
                if (isSpecial) when (val n = str[i]) {
                    c -> emit(n)
                    in '0'..'9', in 'a'..'f' -> {
                        if (doSpecial) setColor(n)
                    }
                    'k' -> if (doSpecial) obfu = true
                    'l' -> if (doSpecial) bold = true
                    'm' -> if (doSpecial && strike < 0) strike = getLength()
                    'n' -> if (doSpecial && under < 0) under = getLength()
                    'o' -> if (doSpecial) ital = true
                    'r' -> if (doSpecial) reset()
                    else -> isSpecial = false
                }
                if (isSpecial) i++
                isSpecial
            }
            if (skip) continue
            emit(c)
        }
        reset()

        decorators = decoratorsList.toTypedArray()
        chars = charsList.toTypedArray()
        cumWidth = cumWidthList.toTypedArray()
    }

    companion object {
        class ColorMap(map: Map<Char, Color>) {
            private val cols = Array(16) { map[(if (it < 10) it + 48 else it + 87).toChar()]!! }
            fun get(c: Char) = cols[if (c.code > 57) c.code - 87 else c.code - 48]
        }
        val COLORS_NORMAL = ColorMap(mapOf(
            '0' to Color(0x000000FFL),
            '1' to Color(0x0000AAFFL),
            '2' to Color(0x00AA00FFL),
            '3' to Color(0x00AAAAFFL),
            '4' to Color(0xAA0000FFL),
            '5' to Color(0xAA00AAFFL),
            '6' to Color(0xFFAA00FFL),
            '7' to Color(0xAAAAAAFFL),
            '8' to Color(0x555555FFL),
            '9' to Color(0x5555FFFFL),
            'a' to Color(0x55FF55FFL),
            'b' to Color(0x55FFFFFFL),
            'c' to Color(0xFF5555FFL),
            'd' to Color(0xFF55FFFFL),
            'e' to Color(0xFFFF55FFL),
            'f' to Color(0xFFFFFFFFL)
        ))
        val COLORS_SHADOW = ColorMap(mapOf(
            '0' to Color(0x000000FFL),
            '1' to Color(0x00002AFFL),
            '2' to Color(0x002A00FFL),
            '3' to Color(0x002A2AFFL),
            '4' to Color(0x2A0000FFL),
            '5' to Color(0x2A002AFFL),
            '6' to Color(0x3F2A00FFL),
            '7' to Color(0x2A2A2AFFL),
            '8' to Color(0x151515FFL),
            '9' to Color(0x15153FFFL),
            'a' to Color(0x153F15FFL),
            'b' to Color(0x153F3FFFL),
            'c' to Color(0x3F1515FFL),
            'd' to Color(0x3F153FFFL),
            'e' to Color(0x3F3F15FFL),
            'f' to Color(0x3F3F3FFFL)
        ))
        const val CHAR_HEIGHT = 8f
        val FONT: FontProvider = MCFontProvider

        val cached = mutableMapOf<StringKey, StringParser>()
        var unused = mutableSetOf<StringKey>()
        fun setUnused() {
            unused = cached.keys.toMutableSet()
        }
        fun removeUnused() {
            unused.forEach { cached.remove(it) }
            unused.clear()
        }

        data class StringKey(val string: String, val parseMode: Int)

        fun parse(s: String, parseMode: Int): List<StringParser> {
            val parts = s.splitToSequence('\n')
            return parts.map {
                val key = StringKey(it, parseMode)
                unused.remove(key)
                cached.getOrPut(key) { StringParser(key) }
            }.toList()
        }
    }
}