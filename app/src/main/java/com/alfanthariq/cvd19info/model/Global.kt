package com.alfanthariq.cvd19info.model

import com.google.gson.annotations.SerializedName

data class Global(
    @SerializedName("NewConfirmed") var NewConfirmed : Int,
    @SerializedName("TotalConfirmed") var TotalConfirmed : Int,
    @SerializedName("NewDeaths") var NewDeaths : Int,
    @SerializedName("TotalDeaths") var TotalDeaths : Int,
    @SerializedName("NewRecovered") var NewRecovered : Int,
    @SerializedName("TotalRecovered") var TotalRecovered : Int,
    @SerializedName("Date") var Date : String
)
