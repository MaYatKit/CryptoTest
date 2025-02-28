package com.example.cryptotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cryptotest.ui.theme.CryptoTestTheme

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
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {

        }
    }
}


