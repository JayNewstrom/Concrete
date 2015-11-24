package com.jaynewstrom.concrete;

import android.content.Context;

import java.util.ArrayList;
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
            objectGraph = ObjectGraph.create(block.daggerModule());
        } else {
            objectGraph = parentWall.objectGraph.plus(block.daggerModule());
        }
        if (validate) {
            objectGraph.validate();
        }
    }

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

    public Context createContext(Context baseContext) {
        return new ConcreteWallContext(baseContext, this);
    }
}
