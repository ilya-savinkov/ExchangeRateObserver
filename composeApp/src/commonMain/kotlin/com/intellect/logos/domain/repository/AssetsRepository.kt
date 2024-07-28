package com.intellect.logos.domain.repository

import androidx.paging.PagingData
import com.intellect.logos.domain.model.Asset
import kotlinx.coroutines.flow.Flow

interface AssetsRepository {
    suspend fun getAssets(query: String): Flow<PagingData<Asset>>
    suspend fun getAsset(name: String): Asset
    suspend fun setDefaultAsset(asset: String, type: Asset.Type)
    suspend fun getDefaultAssets(): Flow<Pair<Asset, Asset>>
    suspend fun loadAssets(): Result<Unit>
}