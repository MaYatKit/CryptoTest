package com.example.cryptotest.data.model


data class Currency(
    val coinId: String,
    val name: String,
    val symbol: String,
    val tokenDecimal: Int,
    val contractAddress: String,
    val withdrawalEta: List<String>,
    val colorfulImageUrl: String, 
    val grayImageUrl: String, 
    val hasDepositAddressTag: Boolean, 
    val minBalance: Double, 
    val blockchainSymbol: String, 
    val tradingSymbol: String, 
    val code: String,
    val explorer: String,
    val isErc20: Boolean, 
    val gasLimit: Double,
    val tokenDecimalValue: String, 
    val displayDecimal: Double,
    val supportsLegacyAddress: Boolean, 
    val depositAddressTagName: String, 
    val depositAddressTagType: String, 
    val numConfirmationRequired: Double,
)
