package com.jaynewstrom.concretetesting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.jaynewstrom.concrete.Concrete;

import javax.inject.Inject;
import javax.inject.Named;

public final class ExampleView extends View {

    @Inject String injectedString;
    @Inject @Named("child") String injectedChildString;

    public ExampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Concrete.<ContextChildTestComponent>getComponent(context).inject(this);
    }
}
