package com.jaynewstrom.concretesample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concrete.ConcreteWall;

public abstract class BaseActivity extends AppCompatActivity {

    private ConcreteWall activityConcreteWall;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityConcreteWall = Concrete.findWall(getApplicationContext()).stack(concreteBlock());
        Concrete.inject(this, this);
    }

    protected abstract ConcreteBlock concreteBlock();

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
