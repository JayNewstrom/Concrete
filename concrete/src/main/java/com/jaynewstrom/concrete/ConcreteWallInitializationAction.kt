package com.jaynewstrom.concrete

/**
 * Can be used when calling [ConcreteWall.stack] and will be called when the wall is created.
 */
interface ConcreteWallInitializationAction<in C> {
    /**
     * Called exactly once for each [ConcreteWall] instance.
     */
    fun onWallInitialized(wall: ConcreteWall<C>)
}
