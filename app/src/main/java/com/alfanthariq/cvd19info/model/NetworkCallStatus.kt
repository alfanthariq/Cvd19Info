package com.alfanthariq.cvd19info.model

data class NetworkCallStatus(
        var status : Boolean,
        var errMsg : String,
        var responseCode : Int
)
