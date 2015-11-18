package com.jaynewstrom.concretesample.application;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module(
        library = true
)
public final class ApplicationModule {

    private final Application application;

    ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides @ForApplication Context provideApplicationContext() {
        return application;
    }
}
