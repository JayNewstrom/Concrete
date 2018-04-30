package com.jaynewstrom.concretetesting

import dagger.Component

@ForChild
@Component(dependencies = [ContextTestComponent::class], modules = [ContextChildTestModule::class])
internal interface ContextChildTestComponent {
    fun inject(exampleView: ExampleView)
}
