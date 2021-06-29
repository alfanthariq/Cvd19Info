package com.alfanthariq.cvd19info.di

import android.app.Application
import android.content.Context
import com.alfanthariq.cvd19info.R
import com.alfanthariq.cvd19info.base.BaseApplication
import com.alfanthariq.cvd19info.utils.LocalizationUtil
import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import javax.inject.Singleton

@Module
class AppModule(val apps : Application) {
    @Provides
    @Singleton
    fun provideApplication() : Application {
        return apps
    }
}