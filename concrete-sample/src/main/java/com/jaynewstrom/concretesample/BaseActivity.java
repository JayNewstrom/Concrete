package com.jaynewstrom.concretesample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concrete.ConcreteWall;
import com.jaynewstrom.concretesample.application.ApplicationComponent;

public abstract class BaseActivity<C> extends AppCompatActivity {

    private ConcreteWall<C> activityConcreteWall;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConcreteWall<ApplicationComponent> applicationWall = Concrete.findWall(getApplicationContext());
        activityConcreteWall = applicationWall.stack(concreteBlock(applicationWall.getComponent()));
        performInject(activityConcreteWall.getComponent());
    }

    protected abstract ConcreteBlock<C> concreteBlock(ApplicationComponent applicationComponent);

    protected abstract void performInject(C component);

    @Override public Object getSystemService(@NonNull String name) {
        if (Concrete.isService(name)) {
            return activityConcreteWall;
        }
        return super.getSystemService(name);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            activityConcreteWall.destroy();
        }
    }
}
