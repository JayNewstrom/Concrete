package com.jaynewstrom.concretesample.details;

import com.jaynewstrom.concretesample.application.ApplicationModule;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                DetailActivity.class,
                DetailsListItemView.class,
        },
        addsTo = ApplicationModule.class
)
final class DetailModule {

    private final String detailsTitle;

    DetailModule(String detailsTitle) {
        this.detailsTitle = detailsTitle;
    }

    @Provides @Named("detailsTitle") String provideDetailsTitle() {
        return detailsTitle;
    }
}
