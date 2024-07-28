package com.intellect.logos.domain.usecase.assets

import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository

class GetAssetUseCase(private val assetsRepository: AssetsRepository) {

    suspend operator fun invoke(name: String): Asset = assetsRepository.getAsset(name)
}