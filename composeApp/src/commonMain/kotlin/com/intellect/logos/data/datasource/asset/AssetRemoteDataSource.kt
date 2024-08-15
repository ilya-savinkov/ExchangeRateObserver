package com.intellect.logos.data.datasource.asset

import ServerConstant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import response.AssetResponse

@Single
class AssetRemoteDataSource(@Provided private val httpClient: HttpClient) {

    suspend fun getAsset(name: String): Result<AssetResponse> = runCatching {
        httpClient
            .get("${ServerConstant.API_V1}/asset/$name")
            .body()
    }

    suspend fun getAssets(page: Int, pageSize: Int): List<AssetResponse> {
        return httpClient.get("${ServerConstant.API_V1}/assets") {
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()
    }
}