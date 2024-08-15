package com.intellect.logos.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.intellect.logos.data.datasource.asset.AssetLocalDataSource
import com.intellect.logos.data.datasource.asset.AssetRemoteDataSource
import com.intellect.logos.data.datasource.asset.AssetsRemoteMediator
import com.intellect.logos.data.db.entity.AssetEntity
import com.intellect.logos.data.mapper.toDomain
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [AssetsRepository::class])
class AssetsRepositoryImpl(
    private val assetRemoteDataSource: AssetRemoteDataSource,
    private val assetLocalDataSource: AssetLocalDataSource
) : AssetsRepository {
    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 5
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getAssets(): Flow<PagingData<Asset>> {
        val pagingSource = assetLocalDataSource.getAssets()

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = {
                pagingSource
            },
            remoteMediator = AssetsRemoteMediator(
                assetsRemoteDataSource = assetRemoteDataSource,
                assetLocalDataSource = assetLocalDataSource
            )
        ).flow.map { pagingData ->
            pagingData.map(AssetEntity::toDomain)
        }
    }

    override suspend fun getAsset(name: String): Result<Asset> {
        val cachedAsset = assetLocalDataSource.getAsset(name)

        return if (cachedAsset != null) {
            Result.success(cachedAsset.toDomain())
        } else {
            assetRemoteDataSource.getAsset(name).map { assetResponse ->
                assetResponse.toDomain()
            }
        }
    }


    override suspend fun setDefaultAsset(asset: String, type: Asset.Type) {
        assetLocalDataSource.setDefaultAsset(
            asset = asset,
            type = type
        )
    }

    override suspend fun getDefaultAssetsFlow(): Flow<Result<Pair<Asset, Asset>>> {
        return assetLocalDataSource.getDefaultAssetsFlow().map { (baseAssetName, quoteAssetName) ->
            val baseAsset = getAsset(baseAssetName).getOrElse {
                return@map Result.failure(it)
            }

            val quoteAsset = getAsset(quoteAssetName).getOrElse {
                return@map Result.failure(it)
            }

            Result.success(baseAsset to quoteAsset)
        }
    }

    override suspend fun swap() {
        assetLocalDataSource.swap()
    }
}