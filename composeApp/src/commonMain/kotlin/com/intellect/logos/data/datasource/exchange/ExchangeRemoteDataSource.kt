package com.intellect.logos.data.datasource.exchange

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import response.RateResponse

class ExchangeRemoteDataSource(private val httpClient: HttpClient) {

    suspend fun getRate(from: String, to: String): RateResponse {
        return httpClient.get("rate?from=$from&to=$to")
            .body<RateResponse>()
    }
}