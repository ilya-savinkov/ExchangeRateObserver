package com.intellect.logos.data.datasource.exchange

import ServerConstant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import response.RateResponse

@Single
class ExchangeRemoteDataSource(@Provided private val httpClient: HttpClient) {

    suspend fun getRate(
        baseAssetName: String,
        quoteAssetName: String
    ): Result<RateResponse> = runCatching {
        httpClient.get("${ServerConstant.API_V1}/rate") {
            parameter("baseAssetName", baseAssetName)
            parameter("quoteAssetName", quoteAssetName)
        }.body<RateResponse>()
    }
}