package com.jaynewstrom.concretesample.details;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
final class DetailModule {

    private final String detailsTitle;

    DetailModule(String detailsTitle) {
        this.detailsTitle = detailsTitle;
    }

    @Provides @Named("detailsTitle") String provideDetailsTitle() {
        return detailsTitle;
    }
}
