package com.jaynewstrom.concrete;

import android.annotation.SuppressLint;
import android.content.Context;

import static java.lang.String.format;

public final class Concrete {

    static final String CONCRETE_SERVICE = Concrete.class.getName();

    /**
     * This creates the foundational wall.
     */
    public static <C> ConcreteWall<C> pourFoundation(C component) {
        Preconditions.checkNotNull(component, "component == null");
        return new ConcreteWall<>(null, new FoundationConcreteBlock<>(component));
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
    public static <T extends ConcreteWall<?>> T findWall(Context context) {
        //noinspection unchecked
        @SuppressWarnings("ResourceType") @SuppressLint("WrongConstant")
        T wall = (T) context.getSystemService(CONCRETE_SERVICE);
        if (wall == null) {
            throw new IllegalArgumentException(format(
                    "Cannot find wall in %s. Make sure your Activity/Application overrides getSystemService() to return its wall if "
                            + "Concrete.isService(name) is true", context.getClass().getName()
            ));
        }
        return wall;
    }

    /**
     * Calling getComponent will find the wall with the given context, and return the component associated with the wall.
     *
     * @throws IllegalStateException if the wall associated with the context is destroyed.
     */
    public static <C> C getComponent(Context context) {
        return Concrete.<ConcreteWall<C>>findWall(context).getComponent();
    }

    private Concrete() {
        throw new AssertionError("No instances.");
    }
}
