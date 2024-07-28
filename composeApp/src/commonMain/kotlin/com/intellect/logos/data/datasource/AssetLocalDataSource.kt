package com.intellect.logos.data.datasource

import androidx.paging.PagingSource
import com.intellect.logos.data.datasource.pagingsource.AssetPagingSource
import com.intellect.logos.data.db.dao.AssetDao
import com.intellect.logos.data.db.entity.AssetEntity

class AssetLocalDataSource(
    private val assetDao: AssetDao
) {
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

    // TODO save default assets in datastore, and get from there
    fun getDefaultAssets(): Pair<String, String> {
        return "EUR" to "USD"
    }

    suspend fun clearAll() {
        assetDao.clearAll()
    }
}