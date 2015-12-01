package com.jaynewstrom.concrete;

public interface ConcreteBlock {

    /**
     * Return a name that will be used to uniquely identify and cache the wall created by this block.
     */
    String name();

    /**
     * Return the module that will define the behavior of the wall created by this block.
     *
     * @see dagger.ObjectGraph#plus(Object...)
     */
    Object daggerModule();
}
