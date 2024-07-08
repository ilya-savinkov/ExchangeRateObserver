package com.intellect.logos.domain.usecase.assets

import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository

class GetAssetsUseCase(private val assetsRepository: AssetsRepository) {
    suspend operator fun invoke(query: String): Result<List<Asset>> {
        return assetsRepository.getAssets(query.trim())
    }
}