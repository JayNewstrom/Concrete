package com.jaynewstrom.concretetesting;

import dagger.Module;
import dagger.Provides;

@Module
final class ContextTestModule {
    @Provides String provideString() {
        return "Context test string";
    }
}
