package com.intellect.logos.domain.repository

interface ExchangeRepository {

    suspend fun getRate(
        baseAssetName: String,
        quoteAssetName: String
    ): Result<Double>
}