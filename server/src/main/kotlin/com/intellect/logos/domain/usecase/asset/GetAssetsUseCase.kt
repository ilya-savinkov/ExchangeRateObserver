package com.intellect.logos.domain.usecase.asset

import com.intellect.logos.domain.repository.AssetRepository
import org.koin.core.annotation.Factory
import response.AssetResponse

@Factory
class GetAssetsUseCase(private val assetRepository: AssetRepository) {

    operator fun invoke(page: Long, pageSize: Int): List<AssetResponse> {
        return assetRepository.getAssets(
            page = page,
            pageSize = pageSize
        )
    }
}