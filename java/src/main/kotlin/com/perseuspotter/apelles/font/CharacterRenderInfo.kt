package com.perseuspotter.apelles.font

data class CharacterRenderInfo(val info: CharacterInfo, val x: Int, val y: Int, val co: Char, val ital: Boolean) : CharacterRenderInfoWrapped {
    override fun get(): CharacterRenderInfo = this
}