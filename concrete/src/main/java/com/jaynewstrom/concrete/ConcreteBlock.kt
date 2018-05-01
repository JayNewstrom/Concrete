package com.jaynewstrom.concrete

interface ConcreteBlock<out C> {
    /**
     * Return a name that will be used to uniquely identify and cache the wall created by this block.
     */
    fun name(): String

    /**
     * Create the component that will define the behavior of the wall created by this block.
     */
    fun createComponent(): C
}
