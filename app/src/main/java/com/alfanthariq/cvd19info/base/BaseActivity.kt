package com.alfanthariq.cvd19info.base

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewbinding.ViewBinding
import com.alfanthariq.cvd19info.R
import com.alfanthariq.cvd19info.utils.LocalizationUtil
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.util.*

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity(), BaseView {
    abstract fun nightMode(): Int

    abstract var viewModel: VM
    private var pDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        AppCompatDelegate.setDefaultNightMode(
            nightMode()
        )

        super.onCreate(savedInstanceState, persistentState)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(
            ViewPumpContextWrapper.wrap(
                LocalizationUtil.wrapContext(newBase!!)
            )
        )
    }

    override fun showLoadingDialog(message: String?) {
        if (pDialog == null) pDialog = Dialog(this)

        pDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pDialog?.setContentView(R.layout.dialog_loading)
        pDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val msg = pDialog?.findViewById(R.id.txt_message) as TextView
        if (message != null) msg.text = message
        else msg.text = getString(R.string.loading)
        pDialog?.setCancelable(false)
        pDialog?.show()
    }

    override fun hideLoadingDialog() {
        if (pDialog != null && pDialog?.isShowing!!) {
            pDialog?.dismiss(); pDialog = null
        }
    }

    inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
        crossinline bindingInflater: (LayoutInflater) -> T
    ) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)
        }

    @Suppress("DEPRECATION")
    fun ContextWrapper.wrap(language: String): ContextWrapper {
        val config = baseContext.resources.configuration
        val sysLocale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.getSystemLocale()
        } else {
            this.getSystemLocaleLegacy()
        }

        if (language.isNotEmpty() && sysLocale.language != language) {
            val locale = Locale(language)
            Locale.setDefault(locale)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.setSystemLocale(locale)
            } else {
                this.setSystemLocaleLegacy(locale)
            }
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val context = baseContext.createConfigurationContext(config)
            ContextWrapper(context)
        } else {
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
            ContextWrapper(baseContext)
        }

    }

    @Suppress("DEPRECATION")
    fun ContextWrapper.getSystemLocaleLegacy(): Locale {
        val config = baseContext.resources.configuration
        return config.locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun ContextWrapper.getSystemLocale(): Locale {
        val config = baseContext.resources.configuration
        return config.locales[0]
    }


    @Suppress("DEPRECATION")
    fun ContextWrapper.setSystemLocaleLegacy(locale: Locale) {
        val config = baseContext.resources.configuration
        config.locale = locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun ContextWrapper.setSystemLocale(locale: Locale) {
        val config = baseContext.resources.configuration
        config.setLocale(locale)
    }
}