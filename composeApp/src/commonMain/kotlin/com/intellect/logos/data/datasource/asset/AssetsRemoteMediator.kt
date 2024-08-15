package com.intellect.logos.data.datasource.asset

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.intellect.logos.data.db.entity.AssetEntity
import com.intellect.logos.data.mapper.toEntities
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalPagingApi::class)
class AssetsRemoteMediator(
    private val assetsRemoteDataSource: AssetRemoteDataSource,
    private val assetLocalDataSource: AssetLocalDataSource
) : RemoteMediator<Int, AssetEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = 7.days
        val lastUpdate = Instant.fromEpochSeconds(assetLocalDataSource.getLastUpdate())
        val cacheDuration = Clock.System.now() - lastUpdate

        return if (cacheDuration > cacheTimeout) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AssetEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    (lastItem.id / state.config.pageSize) + 1
                }
            }

            val entities = assetsRemoteDataSource.getAssets(
                pageSize = state.config.pageSize,
                page = page.toInt()
            ).toEntities()

            if (loadType == LoadType.REFRESH) {
                assetLocalDataSource.clearAll()
            }

            assetLocalDataSource.saveAssets(entities)
            return MediatorResult.Success(endOfPaginationReached = entities.size < state.config.pageSize)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}