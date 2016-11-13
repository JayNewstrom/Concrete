package com.jaynewstrom.concretesample.details;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concretesample.application.ApplicationComponent;

final class DetailConcreteBlock implements ConcreteBlock<DetailComponent> {

    private final ApplicationComponent applicationComponent;
    private final String detailsTitle;

    DetailConcreteBlock(ApplicationComponent applicationComponent, String detailsTitle) {
        this.applicationComponent = applicationComponent;
        this.detailsTitle = detailsTitle;
    }

    @Override public String name() {
        // Appending the details title to the class name allows us to have two Details activities, displaying different data,
        // each having their own ConcreteWall.
        return getClass().getName() + detailsTitle;
    }

    @Override public DetailComponent createComponent() {
        return DaggerDetailComponent.builder()
                .applicationComponent(applicationComponent)
                .detailModule(new DetailModule(detailsTitle))
                .build();
    }
}
