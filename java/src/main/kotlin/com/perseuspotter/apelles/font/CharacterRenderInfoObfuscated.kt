package com.perseuspotter.apelles.font

data class CharacterRenderInfoObfuscated(val info: CharacterInfo, val x: Int, val y: Int, val co: Char, val ital: Boolean) : CharacterRenderInfoWrapped {
    override fun get(): CharacterRenderInfo {
        val newC = StringParser.FONT.getObfuChar(info.w)
        val info = StringParser.FONT.getInfo(newC)
        return CharacterRenderInfo(
            info,
            x, y, co, ital
        )
    }
    override fun getCharInfo(): CharacterInfo = info
}