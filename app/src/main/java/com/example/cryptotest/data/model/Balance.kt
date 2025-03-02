package com.example.cryptotest.data.model

import com.google.gson.annotations.SerializedName

data class Balance(
    @SerializedName("currency") val currency: String,
    @SerializedName("amount") val amount: Double,
)
