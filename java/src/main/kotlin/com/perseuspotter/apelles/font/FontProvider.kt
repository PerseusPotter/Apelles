package com.perseuspotter.apelles.font

abstract class FontProvider(protected val doCache: Boolean) {
    protected val cache = mutableMapOf<Char, CharacterInfo>()
    protected val widthMap = Array(16) { mutableListOf<Char>() }
    fun getInfo(c: Char) = if (doCache) cache.getOrPut(c) { getInfoImpl(c) } else getInfoImpl(c)
    protected abstract fun getInfoImpl(c: Char): CharacterInfo
    protected fun addObfuChar(c: Char) {
        val w = getInfo(c).w
        if (w in 1..15) addObfuChar(c, w)
    }
    protected fun addObfuChar(c: Char, w: Int) {
        widthMap[w].add(c)
    }
    fun getObfuChar(c: Char): Char {
        val n = getObfuChar(getInfo(c).w)
        return if (n == '\u0000') c else n
    }
    fun getObfuChar(w: Int): Char = widthMap.getOrNull(w)?.randomOrNull() ?: '\u0000'
}