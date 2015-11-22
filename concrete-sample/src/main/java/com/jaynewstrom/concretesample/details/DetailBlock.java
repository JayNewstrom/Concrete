package com.jaynewstrom.concretesample.details;

import com.jaynewstrom.concrete.ConcreteBlock;

final class DetailBlock implements ConcreteBlock {

    private final String detailsTitle;

    public DetailBlock(String detailsTitle) {
        this.detailsTitle = detailsTitle;
    }

    @Override public String blockName() {
        // Appending the details title to the class name allows us to have two Details activities, displaying different data,
        // each having their own ConcreteWall.
        return getClass().getName() + detailsTitle;
    }

    @Override public Object module() {
        return new DetailModule(detailsTitle);
    }
}
