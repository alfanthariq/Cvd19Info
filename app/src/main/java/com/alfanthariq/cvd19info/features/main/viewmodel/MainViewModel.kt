package com.alfanthariq.cvd19info.features.main.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.alfanthariq.cvd19info.base.ApiService
import com.alfanthariq.cvd19info.base.BaseViewModel
import com.alfanthariq.cvd19info.model.NetworkCallStatus
import com.alfanthariq.cvd19info.model.Summary
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class MainViewModel : BaseViewModel() {
    @Inject
    lateinit var service: ApiService
    @Inject
    lateinit var apps : Application

    val summaryData = MutableLiveData<Summary?>()
    val networkStatus = MutableLiveData<NetworkCallStatus>()
    var selectedCode = "ID"
    val logoutStatus = MutableLiveData<Boolean>()

    fun getSummary() {
        CoroutineScope(Dispatchers.IO).launch {
            val request = service.getSummary()
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()

                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            summaryData.postValue(response.body())
                        } else {
                            summaryData.postValue(null)
                            networkStatus.postValue(NetworkCallStatus(false, response.message(), response.code()))
                        }
                    } else {
                        response.errorBody()
                        summaryData.postValue(null)
                        networkStatus.postValue(NetworkCallStatus(false, response.message(), response.code()))
                    }
                } catch (e: HttpException) {
                    println(e.message())
                    summaryData.postValue(null)
                    networkStatus.postValue(NetworkCallStatus(false, e.message(), 0))
                } catch (e: Throwable) {
                    println(e.message)
                    summaryData.postValue(null)
                    networkStatus.postValue(NetworkCallStatus(false, e.message!!, 0))
                }
            }
        }
    }

    fun logout(token : String){
        Firebase.auth.signOut()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()

        val client = GoogleSignIn.getClient(apps.applicationContext, gso)
        client.signOut().addOnCompleteListener {
            if (it.isSuccessful) {
                logoutStatus.postValue(true)
            } else {
                logoutStatus.postValue(false)
            }
        }
    }
}