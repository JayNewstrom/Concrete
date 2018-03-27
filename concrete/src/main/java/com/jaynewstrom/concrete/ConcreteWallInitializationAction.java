package com.jaynewstrom.concrete;

/**
 * Can be used when calling {@link ConcreteWall#stack} and will be called when the wall is created.
 */
public interface ConcreteWallInitializationAction<C> {
    /**
     * Called exactly once for each {@link ConcreteWall} instance.
     */
    void onWallInitialized(ConcreteWall<C> wall);
}
