package com.jaynewstrom.concretesample.main;

import com.jaynewstrom.concrete.ConcreteBlock;

final class MainActivityConcreteBlock implements ConcreteBlock {

    @Override public String name() {
        return getClass().getName();
    }

    @Override public Object daggerModule() {
        return new MainActivityModule();
    }
}
