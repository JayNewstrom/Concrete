package com.jaynewstrom.concrete

import android.content.Context

class ConcreteWall<out C> internal constructor(
    private val parentWall: ConcreteWall<*>?,
    private val block: ConcreteBlock<C>
) {
    private var destroyed: Boolean = false
    private val childrenWalls: MutableMap<String, ConcreteWall<*>> = LinkedHashMap()
    /**
     * Get the component associated with the wall.
     */
    val component: C = block.createComponent()
        get() {
            if (destroyed) {
                throw IllegalStateException("Concrete wall has been destroyed.")
            }
            return field
        }
    private val destructionActions: MutableSet<ConcreteWallDestructionAction<C>> = LinkedHashSet()

    /**
     * If a wall identified by the blocks name exists as a child, it will be returned.
     * If it doesn't exist it will be created, cached as a child, the initialization action will run, then the wall
     * will be returned.
     */
    @JvmOverloads fun <ChildComponent> stack(
        block: ConcreteBlock<ChildComponent>,
        initializationAction: ((wall: ConcreteWall<ChildComponent>) -> Unit)? = null
    ): ConcreteWall<ChildComponent> {
        @Suppress("UNCHECKED_CAST")
        val existingWall = childrenWalls[block.name()] as ConcreteWall<ChildComponent>?
        return if (existingWall != null) {
            existingWall
        } else {
            val childWall = createAndCacheChildWall(block)
            initializationAction?.invoke(childWall)
            childWall
        }
    }

    private fun <ChildComponent> createAndCacheChildWall(
        block: ConcreteBlock<ChildComponent>
    ): ConcreteWall<ChildComponent> {
        val wall = ConcreteWall(this, block)
        childrenWalls[block.name()] = wall
        return wall
    }

    /**
     * Marks this wall as destroyed. Destroying a wall destroys all of it's children, and removes it from its parent.
     */
    fun destroy() {
        if (!destroyed) {
            val component = component
            destroyed = true
            if (childrenWalls.isNotEmpty()) {
                // We need to copy the collection, so we can iterate over all values and destroy them.
                // Calling destroy on the child, will remove it from our model, which is why we need the copy.
                for (child in ArrayList(childrenWalls.values)) {
                    child.destroy()
                }
            }
            parentWall?.removeChildWall(this)
            for (destructionAction in destructionActions) {
                destructionAction(component)
            }
        }
    }

    private fun removeChildWall(wall: ConcreteWall<*>) {
        childrenWalls.remove(wall.block.name())
    }

    /**
     * Creates a new Context based on the given base context and this wall.
     */
    fun createContext(baseContext: Context): Context {
        if (destroyed) {
            throw IllegalStateException("Concrete wall has been destroyed.")
        }
        return ConcreteWallContext(baseContext, this)
    }

    /**
     * Add a [destructionAction] to the wall, to be called exactly once when the wall is destroyed.
     */
    fun addDestructionAction(destructionAction: (component: C) -> Unit) {
        if (destroyed) {
            throw IllegalStateException("Concrete wall has been destroyed.")
        }
        destructionActions.add(destructionAction)
    }

    /**
     * Remove a [destructionAction] that may or may not have been previously added.
     */
    fun removeDestructionAction(destructionAction: (component: C) -> Unit) {
        if (destroyed) {
            throw IllegalStateException("Concrete wall has been destroyed.")
        }
        destructionActions.remove(destructionAction)
    }
}

private typealias ConcreteWallDestructionAction<C> = (component: C) -> Unit
