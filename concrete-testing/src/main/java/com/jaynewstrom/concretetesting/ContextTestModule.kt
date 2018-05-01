package com.jaynewstrom.concretetesting

import dagger.Module
import dagger.Provides

@Module
internal class ContextTestModule {
    @Provides fun provideString(): String = "Context test string"
}
