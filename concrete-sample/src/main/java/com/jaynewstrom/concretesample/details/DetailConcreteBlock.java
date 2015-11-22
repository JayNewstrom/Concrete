package com.jaynewstrom.concretesample.details;

import com.jaynewstrom.concrete.ConcreteBlock;

final class DetailConcreteBlock implements ConcreteBlock {

    private final String detailsTitle;

    public DetailConcreteBlock(String detailsTitle) {
        this.detailsTitle = detailsTitle;
    }

    @Override public String name() {
        // Appending the details title to the class name allows us to have two Details activities, displaying different data,
        // each having their own ConcreteWall.
        return getClass().getName() + detailsTitle;
    }

    @Override public Object daggerModule() {
        return new DetailModule(detailsTitle);
    }
}
