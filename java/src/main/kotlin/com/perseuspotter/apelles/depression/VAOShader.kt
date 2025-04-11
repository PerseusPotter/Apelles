package com.perseuspotter.apelles.depression

class VAOShader(type: Int) : Shader(
    null,
    javaClass.getResourceAsStream("/shaders/C${if (type and 4 == 4) 1 else 0}N${if (type and 2 == 2) 1 else 0}T${if (type and 1 == 1) 1 else 0}")!!.bufferedReader().use { it.readText() }
) {
    companion object {
        val store = Array(8) { VAOShader(it) }

        fun get(c: Boolean, n: Boolean, t: Boolean) = store[(if (c) 4 else 0) or (if (n) 2 else 0) or (if (t) 1 else 0)]
    }
}