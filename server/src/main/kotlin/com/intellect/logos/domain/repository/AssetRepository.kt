package com.intellect.logos.domain.repository

import response.AssetResponse

interface AssetRepository {
    fun getAssets(page: Long, pageSize: Int): Result<List<AssetResponse>>
    fun getAsset(name: String): Result<AssetResponse?>
}