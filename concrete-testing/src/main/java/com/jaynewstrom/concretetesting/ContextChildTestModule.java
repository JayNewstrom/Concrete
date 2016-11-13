package com.jaynewstrom.concretetesting;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
final class ContextChildTestModule {
    @Provides @Named("child") String provideChildString() {
        return "I'm the child";
    }
}
