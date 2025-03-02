package com.example.cryptotest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotest.data.model.Balance
import com.example.cryptotest.data.model.Currency
import com.example.cryptotest.data.model.CurrencyItem
import com.example.cryptotest.data.model.ExchangeRate
import com.example.cryptotest.data.model.LumpSumItem
import com.example.cryptotest.data.repository.WalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.RoundingMode

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WalletRepository(application.applicationContext)

    private val _currencies = MutableStateFlow<List<Currency>>(emptyList())

    private val _exchangeRates = MutableStateFlow<List<ExchangeRate>>(emptyList())

    private val _walletBalance = MutableStateFlow<List<Balance>>(emptyList())

    val currencyItems = combine(
        _exchangeRates,
        _walletBalance,
        _currencies
    ) { exchangeRates, balances, currencies ->

        val items = mutableListOf<CurrencyItem>()
        currencies.forEach { currency ->
            val balance = balances.firstOrNull { balance ->
                currency.symbol == balance.currency
            } ?: Balance(currency.symbol, 0.0)

            val exchangeRate = exchangeRates.firstOrNull { exchangeRate ->
                currency.symbol == exchangeRate.fromCurrency
            }

            if (exchangeRate != null) {
                val currencyName = currency.name
                val currencySymbol = currency.symbol
                val currencyImageUrl = currency.colorfulImageUrl
                val currencyAmount = balance.amount
                val correctRate = exchangeRate.rates.findLast { rate ->
                    val amountThreshold = rate.amount.toBigDecimalOrNull()
                    if (amountThreshold == null) {
                        false
                    } else {
                        val currencyAmountToBigDecimal = currencyAmount.toBigDecimal()
                        currencyAmountToBigDecimal >= amountThreshold
                    }
                }?.rate?.toBigDecimal() ?: exchangeRate.rates[0].rate.toBigDecimal()


                val currencyToUsdAmount = currencyAmount.toString().toBigDecimal() * correctRate
                val displayCurrencyToUsdAmount = currencyToUsdAmount.setScale(
                    correctRate.scale(),
                    RoundingMode.HALF_UP
                ).stripTrailingZeros().toString()

                val displayCurrencyAmount = currencyAmount.toString().toBigDecimal().setScale(
                    currency.displayDecimal,
                    RoundingMode.HALF_UP
                ).stripTrailingZeros().toString()

                items.add(
                    CurrencyItem(
                        displayCurrencyToUsdAmount,
                        currencyToUsdAmount.toString(),
                        displayCurrencyAmount,
                        currencyImageUrl,
                        currencyName,
                        currencySymbol
                    )
                )
            }
        }
        items
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(300),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val lumpSumItem = currencyItems.mapLatest { items ->

        var usdLumpSum = 0.0

        items.forEach { item ->
            usdLumpSum += item.currencyToUsdAmount.toDoubleOrNull() ?: 0.0
        }

        val displayUsdLumpSum = usdLumpSum.toString().toBigDecimal().stripTrailingZeros().toString()

        LumpSumItem(usdLumpSum = displayUsdLumpSum)
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _currencies.value = repository.getCurrencies()
            _exchangeRates.value = repository.getExchangeRates()
            _walletBalance.value = repository.getWalletBalances()
        }
    }


}
