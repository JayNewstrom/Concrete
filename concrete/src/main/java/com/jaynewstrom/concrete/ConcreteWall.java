package com.jaynewstrom.concrete;

import android.content.Context;

import java.util.LinkedHashMap;
import java.util.Map;

import dagger.ObjectGraph;

public final class ConcreteWall {

    private boolean destroyed;

    private final ConcreteWall parent;
    private final ConcreteBlock block;
    private final Map<String, ConcreteWall> children;
    private final ObjectGraph objectGraph;
    private final boolean validate;

    ConcreteWall(ConcreteWall parent, ConcreteBlock block, boolean validate) {
        this.parent = parent;
        this.block = block;
        this.validate = validate;
        this.children = new LinkedHashMap<>();
        if (parent == null) {
            objectGraph = ObjectGraph.create(block.module());
        } else {
            objectGraph = parent.objectGraph.plus(block.module());
        }
        if (validate) {
            objectGraph.validate();
        }
    }

    public ConcreteWall stack(ConcreteBlock block) {
        if (parent != null) {
            ConcreteWall existingWall = parent.children.get(block.blockName());
            if (existingWall != null) {
                return existingWall;
            } else {
                return createAndCacheChild(block);
            }
        }
        return createAndCacheChild(block);
    }

    private ConcreteWall createAndCacheChild(ConcreteBlock block) {
        ConcreteWall wall = new ConcreteWall(this, block, validate);
        children.put(block.blockName(), wall);
        return wall;
    }

    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            if (parent != null) {
                parent.removeChild(this);
            }
        }
    }

    private void removeChild(ConcreteWall concreteWall) {
        children.remove(concreteWall.block.blockName());
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
