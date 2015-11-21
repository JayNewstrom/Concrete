package com.jaynewstrom.concretesample.main;

import android.os.Bundle;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concretesample.BaseActivity;
import com.jaynewstrom.concretesample.R;

import javax.inject.Inject;
import javax.inject.Named;

public final class MainActivity extends BaseActivity {

    @Inject @Named("title") String title;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(title);
    }

    @Override protected ConcreteBlock getConcreteBlock() {
        return new MainActivityBlock();
    }
}
