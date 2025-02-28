package com.example.cryptotest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotest.data.model.Balance
import com.example.cryptotest.data.model.Currency
import com.example.cryptotest.data.model.ExchangeRate
import com.example.cryptotest.data.repository.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WalletRepository(application.applicationContext)

    private val _currencies = MutableStateFlow<List<Currency>>(emptyList())
    val currencies: StateFlow<List<Currency>> = _currencies

    private val _exchangeRates = MutableStateFlow<List<ExchangeRate>>(emptyList())
    val exchangeRates: StateFlow<List<ExchangeRate>> = _exchangeRates

    private val _walletBalance = MutableStateFlow<List<Balance>>(emptyList())
    val walletBalance: StateFlow<List<Balance>> = _walletBalance

    init {
        viewModelScope.launch {
            _currencies.value = repository.getCurrencies()
            _exchangeRates.value = repository.getExchangeRates()
            _walletBalance.value = repository.getWalletBalances()
        }
    }


}
