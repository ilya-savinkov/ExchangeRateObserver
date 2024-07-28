package com.intellect.logos.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import response.AssetResponse

class AssetRemoteDataSource(private val httpClient: HttpClient) {

    suspend fun getAssets(): List<AssetResponse> {
        return httpClient.get("assets")
            .body<List<AssetResponse>>()
    }
}