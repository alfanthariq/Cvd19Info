package com.alfanthariq.cvd19info.di

import com.alfanthariq.cvd19info.features.intro.viewmodel.IntroViewModel
import com.alfanthariq.cvd19info.features.main.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(viewmodel: MainViewModel)
    fun inject(viewmodel: IntroViewModel)
}