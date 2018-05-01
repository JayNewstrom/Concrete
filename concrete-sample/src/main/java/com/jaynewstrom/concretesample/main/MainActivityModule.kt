package com.jaynewstrom.concretesample.main

import android.content.Context
import com.jaynewstrom.concretesample.R
import com.jaynewstrom.concretesample.application.ForApplication
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class MainActivityModule {
    @Provides @Named("title") fun provideTitle(@ForApplication applicationContext: Context): String {
        return applicationContext.getString(R.string.main_title)
    }
}
