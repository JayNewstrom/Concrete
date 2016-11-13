package com.jaynewstrom.concrete;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ConcreteWall<C> {

    private boolean destroyed;

    private final ConcreteWall<?> parentWall;
    private final ConcreteBlock<C> block;
    private final Map<String, ConcreteWall<?>> childrenWalls;
    private final C component;

    ConcreteWall(ConcreteWall<?> parentWall, ConcreteBlock<C> block) {
        this.parentWall = parentWall;
        this.block = Preconditions.checkNotNull(block, "block == null");
        this.childrenWalls = new LinkedHashMap<>();
        this.component = Preconditions.checkNotNull(block.createComponent(), "block.createComponent() == null");
    }

    /**
     * If a wall identified by the blocks name exists as a child, it will be returned.
     * If it doesn't exist it will be created, cached as a child, then returned.
     */
    public <ChildComponent> ConcreteWall<ChildComponent> stack(ConcreteBlock<ChildComponent> block) {
        //noinspection unchecked
        ConcreteWall<ChildComponent> existingWall = (ConcreteWall<ChildComponent>) childrenWalls.get(block.name());
        if (existingWall != null) {
            return existingWall;
        } else {
            return createAndCacheChildWall(block);
        }
    }

    private <ChildComponent> ConcreteWall<ChildComponent> createAndCacheChildWall(ConcreteBlock<ChildComponent> block) {
        ConcreteWall<ChildComponent> wall = new ConcreteWall<>(this, block);
        childrenWalls.put(block.name(), wall);
        return wall;
    }

    /**
     * Marks this wall as destroyed. Destroying a wall destroys all of it's children, and removes it from its parent.
     */
    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            if (childrenWalls.size() > 0) {
                // We need to copy the collection, so we can iterate over all values and destroy them.
                // Calling destroy on the child, will remove it from our model, which is why we need the copy.
                for (ConcreteWall<?> child : new ArrayList<>(childrenWalls.values())) {
                    child.destroy();
                }
            }
            if (parentWall != null) {
                parentWall.removeChildWall(this);
            }
        }
    }

    private void removeChildWall(ConcreteWall<?> wall) {
        childrenWalls.remove(wall.block.name());
    }

    /**
     * Get the component associated with the wall.
     */
    public C getComponent() {
        if (destroyed) {
            throw new IllegalStateException("Concrete wall has been destroyed.");
        }
        return component;
    }

    /**
     * Creates a new Context based on the given base context and this wall.
     */
    public Context createContext(Context baseContext) {
        return new ConcreteWallContext<>(baseContext, this);
    }
}
