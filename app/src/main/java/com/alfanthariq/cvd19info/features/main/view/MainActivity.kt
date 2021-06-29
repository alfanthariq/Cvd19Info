package com.alfanthariq.cvd19info.features.main.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.alfanthariq.cvd19info.ProjectApplication
import com.alfanthariq.cvd19info.R
import com.alfanthariq.cvd19info.base.BaseActivity
import com.alfanthariq.cvd19info.databinding.ActivityMainBinding
import com.alfanthariq.cvd19info.di.ViewModelFactory
import com.alfanthariq.cvd19info.features.intro.view.IntroActivity
import com.alfanthariq.cvd19info.features.main.viewmodel.MainViewModel
import com.alfanthariq.cvd19info.model.Countries
import com.alfanthariq.cvd19info.utils.DateOperationUtil
import com.murgupluoglu.flagkit.FlagKit
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity<MainViewModel>() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var pref : SharedPreferences
    private val countryItems = ArrayList<IconSpinnerItem>()
    private val countryList = ArrayList<Countries>()

    override fun nightMode(): Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

    override var viewModel: MainViewModel
        get() = ViewModelProvider(this, ViewModelFactory{MainViewModel()})[MainViewModel::class.java]
        set(value) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        (application as ProjectApplication).appComponent.inject(viewModel)

        pref = PreferenceManager.getDefaultSharedPreferences(this)

        init()
    }

    private fun init(){
        binding.spinnerLang.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                    arrayListOf(
                            IconSpinnerItem(iconRes = FlagKit.getResId(context, "id"), text = "ID"),
                            IconSpinnerItem(iconRes = FlagKit.getResId(context, "gb"), text = "EN")
                    )
            )

            val lang = pref.getString("lang", "en")?:"en"
            selectItemByIndex(if (lang == "in") 0 else 1)

            setOnSpinnerItemSelectedListener<IconSpinnerItem>{ oldIndex, oldItem, newIndex, newItem ->
                if (newIndex == 0) {
                    reloadActivity("in")
                } else {
                    reloadActivity("en")
                }
            }

            lifecycleOwner = this@MainActivity
        }

        binding.txtLastUpdate.setOnClickListener {
            showLoadingDialog(getString(R.string.logout_msg))
            viewModel.logout(getString(R.string.token))
        }

        setupObserver()
        showLoadingDialog(getString(R.string.loading))
        viewModel.getSummary()
    }

    @SuppressLint("ApplySharedPref")
    private fun reloadActivity(lang: String) {
        pref.edit().putString("lang", lang).apply()
        recreate()
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserver(){
        viewModel.apply {
            summaryData.observe(this@MainActivity, { data ->
                hideLoadingDialog()
                if (data != null) {
                    countryItems.clear()
                    countryList.clear()
                    countryList.addAll(data.Countries)
                    data.Countries.forEach {
                        val icon = FlagKit.getResId(
                            this@MainActivity,
                            it.CountryCode.toLowerCase(Locale.getDefault())
                        )
                        val ident = resources.getIdentifier(
                            it.CountryCode.toLowerCase(Locale.getDefault()), "drawable",
                            packageName
                        );
                        if (ident != 0)
                            countryItems.add(IconSpinnerItem(iconRes = icon, text = it.Country))
                        else
                            countryItems.add(
                                IconSpinnerItem(
                                    iconRes = R.drawable.eu,
                                    text = it.Country
                                )
                            )
                    }

                    binding.spinnerCountry.apply {
                        setSpinnerAdapter(IconSpinnerAdapter(this))
                        setItems(
                            countryItems
                        )
                        setOnSpinnerItemSelectedListener<IconSpinnerItem> { oldIndex, oldItem, newIndex, newItem ->
                            if (countryItems.isNotEmpty()) {
                                val selectedCountry = countryList[newIndex]
                                viewModel.selectedCode = selectedCountry.CountryCode
                                binding.apply {
                                    val dec = DecimalFormat("#,###.##")
                                    val activeCase =
                                        selectedCountry.TotalConfirmed - selectedCountry.TotalDeaths - selectedCountry.TotalRecovered
                                    txtActivecase.text = dec.format(activeCase)
                                    txtTotalcase.text = dec.format(selectedCountry.TotalConfirmed)
                                    txtRecovered.text = dec.format(selectedCountry.TotalRecovered)
                                    txtDeath.text = dec.format(selectedCountry.TotalDeaths)

                                    txtLastUpdate.text = "${getString(R.string.last_update)} ${
                                        DateOperationUtil.dateStrFormat(
                                            "yyyy-MM-dd'T'HH:mm:ss'Z'",
                                            "dd/MM/yyyy HH:mm",
                                            selectedCountry.Date
                                        )
                                    }"

                                    val percentageActive =
                                        (activeCase.toDouble() / selectedCountry.TotalConfirmed.toDouble()) * 100
                                    val percentageRecover =
                                        (selectedCountry.TotalRecovered.toDouble() / selectedCountry.TotalConfirmed.toDouble()) * 100

                                    txtRatioPercentage.text = "${percentageRecover.toInt()} %"
                                    activeProgress.progress = percentageActive.toInt()
                                    recoveredProgress.progress = percentageRecover.toInt()

                                    txtRecoveredChart.text =
                                        dec.format(selectedCountry.TotalRecovered)
                                    txtAffectedChart.text = dec.format(activeCase)
                                }
                            }
                        }
                        val sel = countryList.find {
                            it.CountryCode == viewModel.selectedCode
                        }
                        selectItemByIndex(countryList.indexOf(sel))
                        lifecycleOwner = this@MainActivity
                    }
                }
            })

            logoutStatus.observe(this@MainActivity, { status ->
                hideLoadingDialog()
                if (status) {
                    startActivity(Intent(this@MainActivity, IntroActivity::class.java))
                    finish()
                }
            })

            networkStatus.observe(this@MainActivity, { status ->
                val errMsg = "${getString(R.string.fail_summary)} (error ${status.responseCode})"
                Toast.makeText(this@MainActivity, errMsg, LENGTH_LONG).show()
            })
        }
    }
}