package com.example.cryptotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptotest.data.model.Balance
import com.example.cryptotest.data.model.Currency
import com.example.cryptotest.data.model.ExchangeRate
import com.example.cryptotest.data.model.Rate
import com.example.cryptotest.ui.theme.CryptoTestTheme
import java.math.RoundingMode

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoTestTheme {
                Wallet(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Wallet(viewModel: MainViewModel) {
    val currencies by viewModel.currencies.collectAsState()
    val exchangeRates by viewModel.exchangeRates.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Wallet") })
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {

        }
    }
}

@Composable
fun WalletBalanceItem(balance: Balance, exchangeRate: ExchangeRate, currency: Currency) {

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
        currency.displayDecimal,
        RoundingMode.HALF_UP
    ).toString()

    val displayCurrencyAmount = currencyAmount.toString().toBigDecimal().setScale(
        currency.displayDecimal,
        RoundingMode.HALF_UP
    ).toString()



    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = currencyName,
                modifier = Modifier.wrapContentSize()
            )

            Column(modifier = Modifier.wrapContentSize()) {
                Text(
                    text = displayCurrencyAmount + currencySymbol,
                    modifier = Modifier.wrapContentSize()
                )
                Text(
                    text = "$ $displayCurrencyToUsdAmount",
                    modifier = Modifier.wrapContentSize()
                )
            }


        }


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun WalletBalanceItemPreview() {
    val balance = Balance("CRO", 259.1)
    val exchangeRate = ExchangeRate(
        "CRO", "USD",
        listOf(Rate("1000", "0.147268")), 1602080062
    )
    val currency = Currency(
        coinId = "BTC",
        name = "Bitcoin",
        symbol = "BTC",
        tokenDecimal = 8,
        contractAddress = "",
        withdrawalEta = listOf("30 secs", "2 mins", "30 mins"),
        colorfulImageUrl = "https://s3-ap-southeast-1.amazonaws.com/monaco-cointrack-production/uploads/coin/colorful_logo/5c1246f55568a400e48ac233/bitcoin.png",
        grayImageUrl = "https://s3-ap-southeast-1.amazonaws.com/monaco-cointrack-production/uploads/coin/gray_logo/5c1246f55568a400e48ac233/bitcoin1.png",
        hasDepositAddressTag = false,
        minBalance = 0.0,
        blockchainSymbol = "BTC",
        tradingSymbol = "BTC",
        code = "BTC",
        explorer = "https://blockchair.com/bitcoin/transaction/",
        isErc20 = false,
        gasLimit = 0,
        tokenDecimalValue = "10000000",
        displayDecimal = 8,
        supportsLegacyAddress = false,
        depositAddressTagName = "",
        depositAddressTagType = "",
        numConfirmationRequired = 1
    )


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Wallet") })
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            WalletBalanceItem(balance, exchangeRate, currency)
        }
    }


}


