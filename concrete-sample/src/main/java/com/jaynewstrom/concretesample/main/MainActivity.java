package com.jaynewstrom.concretesample.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;
import com.jaynewstrom.concretesample.R;

import javax.inject.Inject;
import javax.inject.Named;

public final class MainActivity extends AppCompatActivity {

    @Inject @Named("title") String title;

    private ConcreteWall activityWall;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityWall = Concrete.findWall(getApplicationContext()).stack(new MainActivityBlock());
        Concrete.inject(this, this);
        setTitle(title);
    }

    @Override public Object getSystemService(@NonNull String name) {
        if (Concrete.isConcreteService(name)) {
            return activityWall;
        }
        return super.getSystemService(name);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            activityWall.destroy();
        }
    }
}
