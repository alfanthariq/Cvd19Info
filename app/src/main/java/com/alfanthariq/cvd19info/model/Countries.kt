package com.alfanthariq.cvd19info.model

import com.google.gson.annotations.SerializedName

data class Countries(
    @SerializedName("ID") var ID : String,
    @SerializedName("Country") var Country : String,
    @SerializedName("CountryCode") var CountryCode : String,
    @SerializedName("Slug") var Slug : String,
    @SerializedName("NewConfirmed") var NewConfirmed : Int,
    @SerializedName("TotalConfirmed") var TotalConfirmed : Int,
    @SerializedName("NewDeaths") var NewDeaths : Int,
    @SerializedName("TotalDeaths") var TotalDeaths : Int,
    @SerializedName("NewRecovered") var NewRecovered : Int,
    @SerializedName("TotalRecovered") var TotalRecovered : Int,
    @SerializedName("Date") var Date : String,
    //@SerializedName("Premium") var Premium : Premium
)
