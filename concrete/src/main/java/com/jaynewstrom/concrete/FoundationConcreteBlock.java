package com.jaynewstrom.concrete;

final class FoundationConcreteBlock implements ConcreteBlock {

    private static final String FOUNDATION_NAME = "Foundation";

    private final Object module;

    FoundationConcreteBlock(Object module) {
        this.module = module;
    }

    @Override public String name() {
        return FOUNDATION_NAME;
    }

    @Override public Object daggerModule() {
        return module;
    }
}
