package com.jaynewstrom.concretesample.details

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class DetailModule(private val detailsTitle: String) {
    @Provides @Named("detailsTitle") fun provideDetailsTitle(): String {
        return detailsTitle
    }
}
