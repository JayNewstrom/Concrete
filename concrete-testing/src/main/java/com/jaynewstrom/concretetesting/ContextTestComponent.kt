package com.jaynewstrom.concretetesting

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextTestModule::class])
internal interface ContextTestComponent {
    val string: String
    fun inject(contextTestTarget: ContextTestTarget)
}
