package com.intellect.logos.data.datasource.asset.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.intellect.logos.data.db.dao.AssetDao
import com.intellect.logos.data.db.entity.AssetEntity

class AssetPagingSource(
    private val assetDao: AssetDao,
    private val query: String = ""
) : PagingSource<Int, AssetEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AssetEntity> {
        val page = params.key ?: 0
        val loadSize = params.loadSize

        val assets = if (query.isBlank()) {
            assetDao.getAll(
                page = page,
                loadSize = loadSize
            )
        } else {
            assetDao.search(
                query = query,
                page = page,
                loadSize = loadSize
            )
        }

        return LoadResult.Page(
            data = assets,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (assets.isEmpty()) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, AssetEntity>): Int? = null
}