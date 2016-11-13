package com.jaynewstrom.concretesample.application;

import android.app.Application;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;
import com.squareup.leakcanary.LeakCanary;

public final class ConcreteSampleApplication extends Application {

    private ConcreteWall<ApplicationComponent> foundation;

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        foundation = Concrete.pourFoundation(createApplicationComponent());
    }

    private ApplicationComponent createApplicationComponent() {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    @Override public Object getSystemService(String name) {
        if (Concrete.isService(name)) {
            return foundation;
        }
        return super.getSystemService(name);
    }
}
