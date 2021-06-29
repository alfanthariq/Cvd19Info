package com.alfanthariq.cvd19info.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocalizationUtil {
    fun wrapContext(context: Context): Context {
        val pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        val lang = pref?.getString("lang", "en") ?: return context

        val savedLocale = Locale(lang) // load the user picked language from persistence (e.g SharedPreferences)

        // as part of creating a new context that contains the new locale we also need to override the default locale.
        Locale.setDefault(savedLocale)

        // create new configuration with the saved locale
        val newConfig = Configuration()
        newConfig.setLocale(savedLocale)

        return context.createConfigurationContext(newConfig)
    }
}