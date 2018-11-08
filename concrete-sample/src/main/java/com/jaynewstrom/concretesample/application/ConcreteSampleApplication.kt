package com.jaynewstrom.concretesample.application

import android.app.Application

import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall

class ConcreteSampleApplication : Application() {
    private lateinit var foundation: ConcreteWall<ApplicationComponent>

    override fun onCreate() {
        super.onCreate()
        foundation = Concrete.pourFoundation(createApplicationComponent())
    }

    private fun createApplicationComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

    override fun getSystemService(name: String): Any? {
        return if (Concrete.isService(name)) {
            foundation
        } else {
            super.getSystemService(name)
        }
    }
}
