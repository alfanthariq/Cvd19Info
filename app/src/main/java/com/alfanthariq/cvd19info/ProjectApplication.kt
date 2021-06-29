package com.alfanthariq.cvd19info

import android.app.Application
import android.content.Context
import com.alfanthariq.cvd19info.di.AppComponent
import com.alfanthariq.cvd19info.di.AppModule
import com.alfanthariq.cvd19info.di.DaggerAppComponent
import com.alfanthariq.cvd19info.di.NetworkModule
import com.alfanthariq.cvd19info.utils.LocalizationUtil
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class ProjectApplication : Application() {
    companion object {
        lateinit var instance : ProjectApplication
            private set
    }
    lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule("https://api.covid19api.com/"))
            //.networkModule(NetworkModule("https://api.alfanthariq.com/tts/public/"))
            .build()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(
            LocalizationUtil.wrapContext(base!!)
        )
    }
}