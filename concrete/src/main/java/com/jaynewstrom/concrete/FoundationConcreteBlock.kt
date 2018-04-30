package com.jaynewstrom.concrete

internal class FoundationConcreteBlock<out C>(private val component: C) : ConcreteBlock<C> {
    override fun name(): String = "Foundation"
    override fun createComponent(): C = component
}
