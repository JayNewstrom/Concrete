package com.jaynewstrom.concrete;

final class FoundationConcreteBlock<C> implements ConcreteBlock<C> {

    private static final String FOUNDATION_NAME = "Foundation";

    private final C component;

    FoundationConcreteBlock(C component) {
        this.component = component;
    }

    @Override public String name() {
        return FOUNDATION_NAME;
    }

    @Override public C createComponent() {
        return component;
    }
}
