package com.intellect.logos.domain.repository

import com.intellect.logos.domain.model.Asset

interface AssetsRepository {
    suspend fun getAssets(query: String): Result<List<Asset>>
    suspend fun getAsset(name: String): Result<Asset>
}