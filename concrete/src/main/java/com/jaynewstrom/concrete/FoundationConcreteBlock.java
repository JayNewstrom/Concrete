package com.jaynewstrom.concrete;

final class FoundationConcreteBlock implements ConcreteBlock {

    static final String FOUNDATION_NAME = "Foundation";

    private final Object module;

    FoundationConcreteBlock(Object module) {
        this.module = module;
    }

    @Override public String blockName() {
        return FOUNDATION_NAME;
    }

    @Override public Object module() {
        return module;
    }
}
