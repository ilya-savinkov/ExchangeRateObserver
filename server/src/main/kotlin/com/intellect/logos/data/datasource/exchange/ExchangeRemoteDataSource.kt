package com.intellect.logos.data.datasource.exchange

import com.intellect.logos.data.model.response.ExchangeRateResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class ExchangeRemoteDataSource(@Provided private val exchangeClient: HttpClient) {

    suspend fun getRate(
        baseAssetName: String,
        quoteAssetName: String
    ): Result<Double> = runCatching {
        exchangeClient.get("latest") {
            parameter("base", baseAssetName)
            parameter("symbols", quoteAssetName)
        }
            .body<ExchangeRateResponse>()
            .rates[quoteAssetName] ?: throw IllegalStateException("Rate not found")
    }
}