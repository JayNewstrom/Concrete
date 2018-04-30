package com.jaynewstrom.concrete

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater

internal class ConcreteWallContext<C>(
    baseContext: Context,
    private val wall: ConcreteWall<C>
) : ContextWrapper(baseContext) {
    private var inflater: LayoutInflater? = null

    override fun getSystemService(name: String): Any? {
        if (Concrete.isService(name)) {
            return wall
        }
        if (Context.LAYOUT_INFLATER_SERVICE == name) {
            if (inflater == null) {
                inflater = LayoutInflater.from(baseContext).cloneInContext(this)
            }
            return inflater
        }
        return super.getSystemService(name)
    }
}
