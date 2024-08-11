package com.intellect.logos.data.datasource.exchange

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import response.RateResponse

class ExchangeRemoteDataSource(private val httpClient: HttpClient) {

    suspend fun getRate(baseAssetName: String, quoteAssetName: String): RateResponse {
        return httpClient.get("rate") {
            parameter("baseAssetName", baseAssetName)
            parameter("quoteAssetName", quoteAssetName)
        }.body<RateResponse>()
    }
}