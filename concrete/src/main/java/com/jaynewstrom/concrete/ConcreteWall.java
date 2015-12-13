package com.jaynewstrom.concrete;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import dagger.ObjectGraph;

public final class ConcreteWall {

    private boolean destroyed;

    private final ConcreteWall parentWall;
    private final ConcreteBlock block;
    private final Map<String, ConcreteWall> childrenWalls;
    private final ObjectGraph objectGraph;
    private final boolean validate;

    ConcreteWall(ConcreteWall parentWall, ConcreteBlock block, boolean validate) {
        this.parentWall = parentWall;
        this.block = block;
        this.validate = validate;
        this.childrenWalls = new LinkedHashMap<>();
        if (parentWall == null) {
            objectGraph = createObjectGraph(block.daggerModule());
        } else {
            objectGraph = plusObjectGraph(parentWall.objectGraph, block.daggerModule());
        }
        if (validate) {
            objectGraph.validate();
        }
    }

    private static ObjectGraph createObjectGraph(Object module) {
        if (module instanceof Collection) {
            Collection c = (Collection) module;
            return ObjectGraph.create(c.toArray(new Object[c.size()]));
        } else {
            return ObjectGraph.create(module);
        }
    }

    private static ObjectGraph plusObjectGraph(ObjectGraph parentObjectGraph, Object module) {
        if (module instanceof Collection) {
            Collection c = (Collection) module;
            return parentObjectGraph.plus(c.toArray(new Object[c.size()]));
        } else {
            return parentObjectGraph.plus(module);
        }
    }

    /**
     * If a wall identified by the blocks name exists as a child, it will be returned.
     * If it doesn't exist it will be created, cached as a child, then returned.
     * <p/>
     * The returned wall will have a superset of the behavior of the wall the block was stacked upon.
     */
    public ConcreteWall stack(ConcreteBlock block) {
        ConcreteWall existingWall = childrenWalls.get(block.name());
        if (existingWall != null) {
            return existingWall;
        } else {
            return createAndCacheChildWall(block);
        }
    }

    private ConcreteWall createAndCacheChildWall(ConcreteBlock block) {
        ConcreteWall wall = new ConcreteWall(this, block, validate);
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
                for (ConcreteWall child : new ArrayList<>(childrenWalls.values())) {
                    child.destroy();
                }
            }
            if (parentWall != null) {
                parentWall.removeChildWall(this);
            }
        }
    }

    private void removeChildWall(ConcreteWall wall) {
        childrenWalls.remove(wall.block.name());
    }

    void inject(Object targetInstance) {
        if (destroyed) {
            throw new IllegalStateException("Concrete wall has been destroyed.");
        }
        objectGraph.inject(targetInstance);
    }

    /**
     * Creates a new Context based on the given base context and this wall.
     */
    public Context createContext(Context baseContext) {
        return new ConcreteWallContext(baseContext, this);
    }
}
