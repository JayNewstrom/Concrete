package com.jaynewstrom.concrete;

import android.annotation.SuppressLint;
import android.content.Context;

import static java.lang.String.format;

public final class Concrete {

    static final String CONCRETE_SERVICE = Concrete.class.getName();

    /**
     * This creates the foundational wall.
     * The validate parameter will call ObjectGraph.validate on the root object graph, as well as every object graph that is plus'd
     * when stacking blocks to create walls.
     */
    public static ConcreteWall pourFoundation(Object daggerModule, boolean validate) {
        return new ConcreteWall(null, new FoundationConcreteBlock(daggerModule), validate);
    }

    /**
     * Returns true if the service associated with the given name is provided by concrete.
     */
    public static boolean isService(String name) {
        return CONCRETE_SERVICE.equals(name);
    }

    /**
     * Find wall returns the wall associated with the given context.
     * A wall can be associated with a context either by overriding getSystemService in an Application or Activity subclass, or by
     * calling wall.createContext.
     *
     * @throws IllegalArgumentException if context.getSystemService isn't overridden, returning the concrete wall.
     */
    public static ConcreteWall findWall(Context context) {
        @SuppressWarnings("ResourceType") @SuppressLint("WrongConstant")
        ConcreteWall wall = (ConcreteWall) context.getSystemService(CONCRETE_SERVICE);
        if (wall == null) {
            throw new IllegalArgumentException(format(
                    "Cannot find wall in %s. Make sure your Activity/Application overrides getSystemService() to return its wall if "
                            + "Concrete.isService(name) is true", context.getClass().getName()
            ));
        }
        return wall;
    }

    /**
     * Calling inject will find the wall with the given context, and use the walls object graph to inject the target.
     *
     * @throws IllegalStateException if the wall associated with the context is destroyed.
     */
    public static void inject(Context context, Object target) {
        findWall(context).inject(target);
    }

    private Concrete() {
        throw new AssertionError("No instances.");
    }
}
