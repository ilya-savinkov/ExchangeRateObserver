package com.intellect.logos.domain.usecase.assets

import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository

class GetDefaultAssetsUseCase(private val assetsRepository: AssetsRepository) {

    suspend operator fun invoke(): Pair<Asset, Asset> {
        return assetsRepository.getDefaultAssets()
    }
}