package com.jaynewstrom.concretesample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.concrete.ConcreteWall
import com.jaynewstrom.concretesample.application.ApplicationComponent

abstract class BaseActivity<C> : AppCompatActivity() {
    private lateinit var activityConcreteWall: ConcreteWall<C>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicationWall = Concrete.findWall<ConcreteWall<ApplicationComponent>>(applicationContext)
        activityConcreteWall = applicationWall.stack(concreteBlock(applicationWall.component))
        performInject(activityConcreteWall.component)
    }

    protected abstract fun concreteBlock(applicationComponent: ApplicationComponent): ConcreteBlock<C>

    protected abstract fun performInject(component: C)

    override fun getSystemService(name: String): Any? {
        return if (Concrete.isService(name)) {
            activityConcreteWall
        } else {
            super.getSystemService(name)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            activityConcreteWall.destroy()
        }
    }
}
