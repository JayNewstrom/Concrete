package com.jaynewstrom.concrete;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public final class ConcreteWall<C> {

    private boolean destroyed;

    private final ConcreteWall<?> parentWall;
    private final ConcreteBlock<C> block;
    private final Map<String, ConcreteWall<?>> childrenWalls;
    private final C component;
    private final Set<ConcreteWallDestructionAction<C>> destructionActions;

    ConcreteWall(@Nullable ConcreteWall<?> parentWall, ConcreteBlock<C> block) {
        this.parentWall = parentWall;
        this.block = Preconditions.checkNotNull(block, "block == null");
        Preconditions.checkNotNull(block.name(), "block.name() == null");
        this.childrenWalls = new LinkedHashMap<>();
        this.component = Preconditions.checkNotNull(block.createComponent(), "block.createComponent() == null");
        this.destructionActions = new LinkedHashSet<>();
    }

    /**
     * If a wall identified by the blocks name exists as a child, it will be returned.
     * If it doesn't exist it will be created, cached as a child, then returned.
     */
    public <ChildComponent> ConcreteWall<ChildComponent> stack(ConcreteBlock<ChildComponent> block) {
        return stack(block, null);
    }

    /**
     * If a wall identified by the blocks name exists as a child, it will be returned.
     * If it doesn't exist it will be created, cached as a child, the initialization action will run, then the wall will be returned.
     */
    public <ChildComponent> ConcreteWall<ChildComponent> stack(
            ConcreteBlock<ChildComponent> block,
            @Nullable ConcreteWallInitializationAction<ChildComponent> initializationAction
    ) {
        //noinspection unchecked
        ConcreteWall<ChildComponent> existingWall = (ConcreteWall<ChildComponent>) childrenWalls.get(block.name());
        if (existingWall != null) {
            return existingWall;
        } else {
            ConcreteWall<ChildComponent> childWall = createAndCacheChildWall(block);
            if (initializationAction != null) {
                initializationAction.onWallInitialized(childWall);
            }
            return childWall;
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
            for (ConcreteWallDestructionAction<C> destructionAction : destructionActions) {
                destructionAction.onWallDestroyed(component);
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

    /**
     * Add a {@link ConcreteWallDestructionAction} to the wall, to be called exactly once when the wall is destroyed.
     */
    public void addDestructionAction(ConcreteWallDestructionAction<C> destructionAction) {
        if (destroyed) {
            throw new IllegalStateException("Concrete wall has been destroyed.");
        }
        destructionActions.add(destructionAction);
    }

    /**
     * Remove a {@link ConcreteWallDestructionAction} that may or may not have been previously added.
     */
    public void removeDestructionAction(ConcreteWallDestructionAction<C> destructionAction) {
        if (destroyed) {
            throw new IllegalStateException("Concrete wall has been destroyed.");
        }
        destructionActions.remove(destructionAction);
    }
}
