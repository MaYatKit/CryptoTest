package com.example.cryptotest.data.repository

import android.content.Context
import com.example.cryptotest.data.model.Currency
import com.example.cryptotest.data.model.ExchangeRate
import com.example.cryptotest.data.model.Balance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WalletRepository(private val context: Context) {


    fun getCurrencies(): List<Currency> {
        val json = loadJsonFromAssets("currencies.json")
        val currencyListType = object : TypeToken<List<Currency>>() {}.type
        val gson = Gson()

        val currencies: List<Currency> = gson.fromJson(json, currencyListType)
        return currencies
    }

    fun getExchangeRates(): List<ExchangeRate> {
        val json = loadJsonFromAssets("live-rates.json")
        val exchangeRateListType = object : TypeToken<List<ExchangeRate>>() {}.type
        val gson = Gson()

        val exchangeRates: List<ExchangeRate> = gson.fromJson(json, exchangeRateListType)
        return exchangeRates
    }

    fun getWalletBalances(): List<Balance> {
        val json = loadJsonFromAssets("wallet-balance.json")
        val balanceListType = object : TypeToken<List<Balance>>() {}.type
        val gson = Gson()

        val walletBalance: List<Balance> = gson.fromJson(json, balanceListType)
        return walletBalance
    }

    private fun loadJsonFromAssets(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

}
