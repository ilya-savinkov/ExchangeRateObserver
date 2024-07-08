package com.intellect.logos.data.repository

import ServerConstant
import androidx.compose.ui.text.intl.Locale
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.Country
import com.intellect.logos.domain.model.Currency
import com.intellect.logos.domain.repository.AssetsRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import response.AssetResponse

class AssetsRepositoryImpl(private val httpClient: HttpClient) : AssetsRepository {

    private val assetsMap: MutableMap<String, Asset> = mutableMapOf()
    private val mutex: Mutex = Mutex()

    override suspend fun getAssets(query: String): Result<List<Asset>> = loadAssets().map { assets ->
        // TODO add pagination
        assets
            .filterValues { asset ->
                val currencyCode = asset.currency.code
                val currencyDescription = asset.currency.description.getOrElse(Locale.current.language) {
                    asset.currency.description.getValue("en")
                }

                currencyCode.contains(query, true) || currencyDescription.contains(query, true)
            }
            .values
            .toList()
    }

    override suspend fun getAsset(name: String): Result<Asset> = loadAssets().map {
        it.getValue(name)
    }

    private suspend fun loadAssets(): Result<Map<String, Asset>> = runCatching {
        // TODO add caching
        assetsMap.ifEmpty {
            mutex.withLock {
                val assets = httpClient.get("assets")
                    .body<List<AssetResponse>>()
                    .map {
                        Asset(
                            icon = "${ServerConstant.URL}/${it.icon}".encodeURLPathPart(),
                            volume = "1",
                            currency = Currency(
                                code = it.currency.code,
                                description = it.currency.description
                            ),
                            country = Country(
                                code = it.country.code,
                                description = it.country.description
                            )
                        )
                    }

                assetsMap.apply {
                    clear()
                    putAll(assets.associateBy { it.currency.code })
                }
            }
        }
    }
}