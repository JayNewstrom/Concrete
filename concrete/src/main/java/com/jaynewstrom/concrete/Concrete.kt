package com.jaynewstrom.concrete

import android.content.Context

object Concrete {
    @JvmStatic private val CONCRETE_SERVICE: String = Concrete::class.java.name

    /**
     * This creates the foundational wall.
     */
    @JvmStatic fun <C> pourFoundation(component: C): ConcreteWall<C> {
        return ConcreteWall(null, FoundationConcreteBlock(component))
    }

    /**
     * Returns true if the service associated with the given name is provided by concrete.
     */
    @JvmStatic fun isService(name: String): Boolean {
        return CONCRETE_SERVICE == name
    }

    /**
     * Find wall returns the wall associated with the given context.
     * A wall can be associated with a context either by overriding getSystemService in an Application or Activity subclass, or by
     * calling wall.createContext.
     *
     * @throws IllegalArgumentException if context.getSystemService isn't overridden, returning the concrete wall.
     */
    @JvmStatic fun <T : ConcreteWall<*>> findWall(context: Context): T {
        @Suppress("UNCHECKED_CAST")
        return context.getSystemService(CONCRETE_SERVICE) as T? ?: throw IllegalArgumentException(
            "Cannot find wall in ${context.javaClass.name}. Make sure your Activity/Application overrides " +
                "getSystemService() to return its wall if Concrete.isService(name) is true"
        )
    }

    /**
     * Calling getComponent will find the wall with the given context, and return the getComponent associated with the wall.
     *
     * @throws IllegalStateException if the wall associated with the context is destroyed.
     */
    @JvmStatic fun <C> getComponent(context: Context): C {
        return Concrete.findWall<ConcreteWall<C>>(context).component
    }
}
