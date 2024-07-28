package com.intellect.logos.domain.usecase.assets

import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository

class SetDefaultAssetUseCase(private val assetsRepository: AssetsRepository) {

    suspend operator fun invoke(asset: String, type: Asset.Type) {
        assetsRepository.setDefaultAsset(
            asset = asset,
            type = type
        )
    }
}