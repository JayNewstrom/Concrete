package com.jaynewstrom.concretetesting

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class ContextChildTestModule {
    @Provides @Named("child") fun provideChildString(): String = "I'm the child"
}
