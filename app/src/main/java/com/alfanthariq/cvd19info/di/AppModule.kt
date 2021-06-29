package com.alfanthariq.cvd19info.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val apps : Application) {
    @Provides
    @Singleton
    fun provideApplication() : Application {
        return apps
    }
}