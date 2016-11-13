package com.jaynewstrom.concretesample.main;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concretesample.application.ApplicationComponent;

final class MainActivityConcreteBlock implements ConcreteBlock<MainActivityComponent> {

    private final ApplicationComponent applicationComponent;

    MainActivityConcreteBlock(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public MainActivityComponent createComponent() {
        return DaggerMainActivityComponent.builder().applicationComponent(applicationComponent).build();
    }
}
