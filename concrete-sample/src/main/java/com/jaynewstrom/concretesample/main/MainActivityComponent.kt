package com.jaynewstrom.concretesample.main

import com.jaynewstrom.concretesample.application.ApplicationComponent

import dagger.Component

@ForMainActivity
@Component(dependencies = [ApplicationComponent::class], modules = [MainActivityModule::class])
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}
