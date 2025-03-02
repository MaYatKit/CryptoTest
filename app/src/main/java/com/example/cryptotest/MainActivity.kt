package com.example.cryptotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.cryptotest.data.model.LumpSumItem
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

    val currencyItems by viewModel.currencyItems.collectAsState()
    val lumpSumItem by viewModel.lumpSumItem.collectAsState(LumpSumItem("0.0"))

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Wallet") })
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("$ ")
                            }
                            append(lumpSumItem.usdLumpSum)
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append(" USD")
                            }
                        }, Modifier.fillMaxHeight().fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }
            }

            items(items = currencyItems) { currencyItem ->
                WalletBalanceItem(
                    currencyItem.displayCurrencyToUsdAmount,
                    currencyItem.displayCurrencyAmount,
                    currencyItem.currencyImageUrl,
                    currencyItem.currencyName,
                    currencyItem.currencySymbol
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WalletBalanceItem(
    displayCurrencyToUsdAmount: String, displayCurrencyAmount: String,
    currencyImageUrl: String, currencyName: String, currencySymbol: String
) {

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(100.dp).padding(10.dp)) {
            GlideImage(
                model = currencyImageUrl,
                contentDescription = stringResource(R.string.icon_of_currency),
                modifier = Modifier.wrapContentSize().align(Alignment.CenterVertically),
                contentScale = ContentScale.Crop,
                loading = placeholder(R.drawable.logo),
                failure = placeholder(R.drawable.logo)
            )
            Text(
                text = currencyName,
                modifier = Modifier.wrapContentSize().align(Alignment.CenterVertically)
                    .padding(start = 10.dp),
            )

            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$displayCurrencyAmount $currencySymbol",
                    modifier = Modifier.wrapContentSize()
                )
                Text(
                    text = "$ $displayCurrencyToUsdAmount",
                    modifier = Modifier.wrapContentSize(),
                    color = Color.Gray
                )
            }


        }


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun WalletBalanceItemPreview() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Wallet") })
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            WalletBalanceItem(
                "18.20",
                "18.20",
                "https://s3-ap-southeast-1.amazonaws.com/monaco-cointrack-production/uploads/coin/colorful_logo/5c1246f55568a400e48ac233/bitcoin.png",
                "BTC",
                "BTC"
            )
        }
    }


}


