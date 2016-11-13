package com.jaynewstrom.concretetesting;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ContextTestModule.class)
interface ContextTestComponent {
    void inject(ContextTestTarget contextTestTarget);

    String getString();
}
