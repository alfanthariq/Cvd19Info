package com.alfanthariq.cvd19info.model

import com.google.gson.annotations.SerializedName

data class Summary(
    @SerializedName("ID") var ID : String,
    @SerializedName("Message") var Message : String,
    @SerializedName("Global") var Global : Global,
    @SerializedName("Countries") var Countries : List<Countries>,
    @SerializedName("Date") var Date : String
)
