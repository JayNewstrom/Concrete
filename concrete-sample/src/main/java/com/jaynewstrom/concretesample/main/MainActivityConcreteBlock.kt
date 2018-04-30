package com.jaynewstrom.concretesample.main

import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.concretesample.application.ApplicationComponent

class MainActivityConcreteBlock(
    private val applicationComponent: ApplicationComponent
) : ConcreteBlock<MainActivityComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): MainActivityComponent {
        return DaggerMainActivityComponent.builder().applicationComponent(applicationComponent).build()
    }
}
