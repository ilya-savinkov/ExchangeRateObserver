package com.intellect.logos.domain.usecase.asset

import com.intellect.logos.domain.repository.AssetRepository
import org.koin.core.annotation.Factory
import response.AssetResponse

@Factory
class GetAssetUseCase(private val assetRepository: AssetRepository) {

    operator fun invoke(name: String): Result<AssetResponse?> {
        return assetRepository.getAsset(name)
    }
}