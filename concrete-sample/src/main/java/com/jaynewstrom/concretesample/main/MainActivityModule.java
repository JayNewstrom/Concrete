package com.jaynewstrom.concretesample.main;

import android.content.Context;

import com.jaynewstrom.concretesample.R;
import com.jaynewstrom.concretesample.application.ForApplication;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
final class MainActivityModule {

    @Provides @Named("title") String provideTitle(@ForApplication Context applicationContext) {
        return applicationContext.getString(R.string.main_title);
    }
}
