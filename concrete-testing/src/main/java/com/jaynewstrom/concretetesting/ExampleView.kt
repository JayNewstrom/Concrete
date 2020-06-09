package com.jaynewstrom.concretetesting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.jaynewstrom.concrete.Concrete
import javax.inject.Inject
import javax.inject.Named

class ExampleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    @Inject internal lateinit var injectedString: String
    @Inject @field:Named("child") internal lateinit var injectedChildString: String

    init {
        Concrete.getComponent<ContextChildTestComponent>(context).inject(this)
    }
}
