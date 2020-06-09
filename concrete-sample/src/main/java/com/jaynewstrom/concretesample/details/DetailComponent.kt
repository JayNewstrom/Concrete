package com.jaynewstrom.concretesample.details

import com.jaynewstrom.concretesample.application.ApplicationComponent
import dagger.Component

@ForDetailActivity
@Component(dependencies = [ApplicationComponent::class], modules = [DetailModule::class])
interface DetailComponent {
    fun inject(activity: DetailActivity)
    fun inject(detailsListItemView: DetailsListItemView)
}
