package com.perseuspotter.apelles.depression

class PerFrameCache<T>(initial: T, private val update: () -> T) {
    private var cached: T = initial
    private var updateFrame = 0L

    fun get(): T {
        if (updateFrame != globalFrameCounter) {
            updateFrame = globalFrameCounter
            cached = update()
        }
        return cached
    }

    companion object {
        @JvmStatic
        var globalFrameCounter = 0L

        @JvmStatic
        fun increaseFrameCounter() = globalFrameCounter++
    }
}