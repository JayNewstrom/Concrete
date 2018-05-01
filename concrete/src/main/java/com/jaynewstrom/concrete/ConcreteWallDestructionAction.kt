package com.jaynewstrom.concrete

/**
 * Can be added to a [ConcreteWall] to be called when the wall is destroyed.
 */
interface ConcreteWallDestructionAction<in C> {
    /**
     * Called exactly once when the wall is destroyed.
     */
    fun onWallDestroyed(component: C)
}
