package com.jaynewstrom.concretesample.details

import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.concretesample.application.ApplicationComponent

class DetailConcreteBlock(
    private val applicationComponent: ApplicationComponent,
    private val detailsTitle: String
) : ConcreteBlock<DetailComponent> {
    override fun name(): String {
        // Appending the details title to the class name allows us to have two Details activities, displaying different data,
        // each having their own ConcreteWall.
        return javaClass.name + detailsTitle
    }

    override fun createComponent(): DetailComponent {
        return DaggerDetailComponent.builder()
            .applicationComponent(applicationComponent)
            .detailModule(DetailModule(detailsTitle))
            .build()
    }
}
