package com.jaynewstrom.concrete;

/**
 * Can be added to a {@link ConcreteWall} to be called when the wall is destroyed.
 */
public interface ConcreteWallDestructionAction<C> {
    /**
     * Called exactly once when the wall is destroyed.
     */
    void onWallDestroyed(C component);
}
