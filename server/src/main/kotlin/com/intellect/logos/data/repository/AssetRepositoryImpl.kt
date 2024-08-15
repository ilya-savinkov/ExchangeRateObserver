package com.intellect.logos.data.repository

import com.intellect.logos.data.datasource.asset.AssetLocalDataSource
import com.intellect.logos.domain.repository.AssetRepository
import org.koin.core.annotation.Single
import response.AssetResponse

@Single(binds = [AssetRepository::class])
class AssetRepositoryImpl(
    private val assetLocalDataSource: AssetLocalDataSource
) : AssetRepository {

    override fun getAssets(page: Long, pageSize: Int): List<AssetResponse> {
        return assetLocalDataSource.getAssets(
            page = page,
            pageSize = pageSize
        )
    }

    override fun getAsset(name: String): AssetResponse? {
        return assetLocalDataSource.getAsset(name)
    }
}