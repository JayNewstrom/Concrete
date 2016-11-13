package com.jaynewstrom.concretesample.application;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class
        }
)
public interface ApplicationComponent {
    @ForApplication Context getApplicationContext();
}
