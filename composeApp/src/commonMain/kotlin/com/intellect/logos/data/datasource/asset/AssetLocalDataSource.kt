package com.intellect.logos.data.datasource.asset

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.PagingSource
import com.intellect.logos.data.datasource.asset.pagingsource.AssetPagingSource
import com.intellect.logos.data.db.dao.AssetDao
import com.intellect.logos.data.db.entity.AssetEntity
import com.intellect.logos.domain.model.Asset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AssetLocalDataSource(
    private val assetDao: AssetDao,
    private val dataStore: DataStore<Preferences>
) {
    private val baseAssetKey: Preferences.Key<String> = stringPreferencesKey("base_asset")
    private val quoteAssetKey: Preferences.Key<String> = stringPreferencesKey("quote_asset")

    suspend fun saveAssets(assetEntities: List<AssetEntity>) {
        assetDao.upsertAll(assetEntities)
    }

    fun getAssets(): PagingSource<Int, AssetEntity> {
        return AssetPagingSource(
            assetDao = assetDao
        )
    }

    fun search(query: String): PagingSource<Int, AssetEntity> {
        return AssetPagingSource(
            query = query,
            assetDao = assetDao
        )
    }

    suspend fun getAsset(name: String): AssetEntity {
        return assetDao.getAsset(name)
    }

    suspend fun setDefaultAsset(asset: String, type: Asset.Type) {
        dataStore.edit {
            when (type) {
                Asset.Type.Base -> it[baseAssetKey] = asset
                Asset.Type.Quote -> it[quoteAssetKey] = asset
            }
        }
    }

    suspend fun swap() {
        dataStore.edit {
            val baseAssetName = it[baseAssetKey] ?: "EUR"
            val quoteAssetName = it[quoteAssetKey] ?: "USD"

            it[baseAssetKey] = quoteAssetName
            it[quoteAssetKey] = baseAssetName
        }
    }

    fun getDefaultAssets(): Flow<Pair<String, String>> {
        return dataStore.data.map {
            Pair(
                it[baseAssetKey] ?: "EUR",
                it[quoteAssetKey] ?: "USD"
            )
        }
    }

    suspend fun clearAll() {
        assetDao.clearAll()
    }
}