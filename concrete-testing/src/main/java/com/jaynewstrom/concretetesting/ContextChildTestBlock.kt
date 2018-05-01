package com.jaynewstrom.concretetesting

import com.jaynewstrom.concrete.ConcreteBlock

internal class ContextChildTestBlock(
    private val contextTestComponent: ContextTestComponent
) : ConcreteBlock<ContextChildTestComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): ContextChildTestComponent {
        return DaggerContextChildTestComponent.builder().contextTestComponent(contextTestComponent).build()
    }
}
