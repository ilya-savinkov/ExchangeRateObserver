package com.intellect.logos.domain.usecase.assets

import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository
import kotlinx.coroutines.flow.Flow

class GetDefaultAssetsUseCase(private val assetsRepository: AssetsRepository) {

    suspend operator fun invoke(): Flow<Pair<Asset, Asset>> {
        return assetsRepository.getDefaultAssets()
    }
}