package com.jaynewstrom.concrete;

public interface ConcreteBlock<C> {

    /**
     * Return a name that will be used to uniquely identify and cache the wall created by this block.
     */
    String name();

    /**
     * Create the component that will define the behavior of the wall created by this block.
     */
    C createComponent();
}
