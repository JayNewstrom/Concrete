package com.jaynewstrom.concrete;

import android.annotation.SuppressLint;
import android.content.Context;

import static java.lang.String.format;

public final class Concrete {

    static final String CONCRETE_SERVICE = Concrete.class.getName();

    public static ConcreteWall pourFoundation(Object daggerModule, boolean validate) {
        return new ConcreteWall(null, new FoundationConcreteBlock(daggerModule), validate);
    }

    public static boolean isService(String name) {
        return CONCRETE_SERVICE.equals(name);
    }

    public static ConcreteWall findWall(Context context) {
        @SuppressWarnings("ResourceType") @SuppressLint("WrongConstant")
        ConcreteWall scope = (ConcreteWall) context.getSystemService(CONCRETE_SERVICE);
        if (scope == null) {
            throw new IllegalArgumentException(format(
                    "Cannot find wall in %s. Make sure your Activity overrides getSystemService() to return its scope if "
                            + "Concrete.isService(name) is true", context.getClass().getName()
            ));
        }
        return scope;
    }

    public static void inject(Context context, Object target) {
        findWall(context).inject(target);
    }

    private Concrete() {
        throw new AssertionError("No instances.");
    }
}
