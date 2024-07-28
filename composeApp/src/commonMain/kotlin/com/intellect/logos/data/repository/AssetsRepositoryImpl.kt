package com.intellect.logos.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.intellect.logos.data.datasource.asset.AssetLocalDataSource
import com.intellect.logos.data.datasource.asset.AssetRemoteDataSource
import com.intellect.logos.data.db.entity.AssetEntity
import com.intellect.logos.data.mapper.toDomain
import com.intellect.logos.data.mapper.toEntities
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AssetsRepositoryImpl(
    private val assetRemoteDataSource: AssetRemoteDataSource,
    private val assetLocalDataSource: AssetLocalDataSource
) : AssetsRepository {
    companion object {
        private const val PAGE_SIZE = 20
    }

    override suspend fun getAssets(query: String): Flow<PagingData<Asset>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {
                if (query.isBlank()) {
                    assetLocalDataSource.getAssets()
                } else {
                    assetLocalDataSource.search(query)
                }
            }
        ).flow.map { pagingData ->
            pagingData.map(AssetEntity::toDomain)
        }
    }

    override suspend fun getAsset(name: String): Asset {
        return assetLocalDataSource.getAsset(name).toDomain()
    }

    override suspend fun setDefaultAsset(asset: String, type: Asset.Type) {
        assetLocalDataSource.setDefaultAsset(
            asset = asset,
            type = type
        )
    }

    override suspend fun getDefaultAssets(): Flow<Pair<Asset, Asset>> {
        return assetLocalDataSource.getDefaultAssets().map { (from, to) ->
            Pair(
                assetLocalDataSource.getAsset(from).toDomain(),
                assetLocalDataSource.getAsset(to).toDomain()
            )
        }
    }

    // TODO Использовать кеш, если данные уже загружены
    // TODO Обновлять кеш каждую неделю
    override suspend fun loadAssets(): Result<Unit> = runCatching {
        assetLocalDataSource.clearAll()
        val assetEntities = assetRemoteDataSource.getAssets().toEntities()
        assetLocalDataSource.saveAssets(assetEntities)
    }
}