package com.jaynewstrom.concretetesting;

import com.jaynewstrom.concrete.ConcreteBlock;

final class ContextChildTestBlock implements ConcreteBlock<ContextChildTestComponent> {
    private final ContextTestComponent contextTestComponent;

    ContextChildTestBlock(ContextTestComponent contextTestComponent) {
        this.contextTestComponent = contextTestComponent;
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public ContextChildTestComponent createComponent() {
        return DaggerContextChildTestComponent.builder().contextTestComponent(contextTestComponent).build();
    }
}
