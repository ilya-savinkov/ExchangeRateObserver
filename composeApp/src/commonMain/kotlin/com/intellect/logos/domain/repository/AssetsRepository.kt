package com.intellect.logos.domain.repository

import androidx.paging.PagingData
import com.intellect.logos.domain.model.Asset
import kotlinx.coroutines.flow.Flow

interface AssetsRepository {
    fun getAssets(): Flow<PagingData<Asset>>
    suspend fun getAsset(name: String): Result<Asset>
    suspend fun setDefaultAsset(asset: String, type: Asset.Type)
    suspend fun getDefaultAssetsFlow(): Flow<Result<Pair<Asset, Asset>>>
    suspend fun swap()
}