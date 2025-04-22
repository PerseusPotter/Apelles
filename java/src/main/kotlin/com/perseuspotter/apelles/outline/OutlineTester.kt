package com.perseuspotter.apelles.outline

import net.minecraft.entity.Entity

abstract class OutlineTester {
    protected abstract fun shouldOutline(e: Entity): Boolean
    val whitelist = mutableSetOf<Class<*>>()
    val blacklist = mutableSetOf<Class<*>>()
    fun <T : Entity> addWhitelist(c: Class<T>) = whitelist.add(c)
    fun <T : Entity> addBlacklist(c: Class<T>) = blacklist.add(c)
    fun <T : Entity> removeWhitelist(c: Class<T>) = whitelist.remove(c)
    fun <T : Entity> removeBlacklist(c: Class<T>) = blacklist.remove(c)
    fun test(e: Entity): Boolean {
        if (whitelist.size > 0 && !whitelist.contains(e::class.java)) return false
        if (blacklist.size > 0 && blacklist.contains(e::class.java)) return false
        return shouldOutline(e)
    }

    class Always : OutlineTester() {
        override fun shouldOutline(e: Entity): Boolean = true
    }
    class Never : OutlineTester() {
        override fun shouldOutline(e: Entity): Boolean = false
    }
    class Custom(val func: (Entity) -> Boolean) : OutlineTester() {
        override fun shouldOutline(e: Entity): Boolean = func(e)
    }
}