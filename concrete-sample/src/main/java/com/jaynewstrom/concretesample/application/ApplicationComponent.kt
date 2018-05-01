package com.jaynewstrom.concretesample.application

import android.content.Context
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    @get:ForApplication val applicationContext: Context
}
