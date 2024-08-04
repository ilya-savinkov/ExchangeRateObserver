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
    private val assetFromKey: Preferences.Key<String> = stringPreferencesKey("asset_from")
    private val assetToKey: Preferences.Key<String> = stringPreferencesKey("asset_to")

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
                Asset.Type.Base -> it[assetFromKey] = asset
                Asset.Type.Quote -> it[assetToKey] = asset
            }
        }
    }

    fun getDefaultAssets(): Flow<Pair<String, String>> {
        return dataStore.data.map {
            Pair(
                it[assetFromKey] ?: "EUR",
                it[assetToKey] ?: "USD"
            )
        }
    }

    suspend fun clearAll() {
        assetDao.clearAll()
    }
}