package com.jaynewstrom.concretesample.main;

import com.jaynewstrom.concrete.ConcreteBlock;

final class MainActivityBlock implements ConcreteBlock {

    @Override public String blockName() {
        return getClass().getName();
    }

    @Override public Object module() {
        return new MainActivityModule();
    }
}
