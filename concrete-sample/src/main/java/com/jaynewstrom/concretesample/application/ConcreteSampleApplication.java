package com.jaynewstrom.concretesample.application;

import android.app.Application;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;
import com.jaynewstrom.concretesample.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

public final class ConcreteSampleApplication extends Application {

    private ConcreteWall foundation;

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        foundation = Concrete.pourFoundation(new ApplicationModule(this), BuildConfig.DEBUG);
    }

    @Override public Object getSystemService(String name) {
        if (Concrete.isService(name)) {
            return foundation;
        }
        return super.getSystemService(name);
    }
}
