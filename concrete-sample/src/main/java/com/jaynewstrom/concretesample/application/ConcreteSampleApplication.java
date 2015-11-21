package com.jaynewstrom.concretesample.application;

import android.app.Application;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;
import com.squareup.leakcanary.LeakCanary;

public final class ConcreteSampleApplication extends Application {

    private ConcreteWall foundation;

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        foundation = Concrete.foundation(new ApplicationModule(this));
    }

    @Override public Object getSystemService(String name) {
        if (Concrete.isConcreteService(name)) {
            return foundation;
        }
        return super.getSystemService(name);
    }
}
