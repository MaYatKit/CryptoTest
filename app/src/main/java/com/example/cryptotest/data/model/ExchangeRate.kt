package com.example.cryptotest.data.model

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rates: List<Rate>,
    val timeStamp: Long
)


data class Rate(
    val amount: String,
    val rate: String
)
