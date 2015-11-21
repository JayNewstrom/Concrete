package com.jaynewstrom.concrete;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

final class ConcreteWallContext extends ContextWrapper {

    private final ConcreteWall concreteWall;

    private LayoutInflater inflater;

    ConcreteWallContext(Context baseContext, ConcreteWall concreteWall) {
        super(baseContext);
        this.concreteWall = concreteWall;
    }

    @Override public Object getSystemService(String name) {
        if (Concrete.isConcreteService(name)) {
            return concreteWall;
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
