package com.jaynewstrom.concrete;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

final class ConcreteWallContext extends ContextWrapper {

    private final ConcreteWall wall;

    private LayoutInflater inflater;

    ConcreteWallContext(Context baseContext, ConcreteWall wall) {
        super(baseContext);
        this.wall = wall;
    }

    @Override public Object getSystemService(String name) {
        if (Concrete.isService(name)) {
            return wall;
        }
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (inflater == null) {
                inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return inflater;
        }
        return super.getSystemService(name);
    }
}
