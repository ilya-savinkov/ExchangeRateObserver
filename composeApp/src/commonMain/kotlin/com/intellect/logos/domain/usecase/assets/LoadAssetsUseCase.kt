package com.intellect.logos.domain.usecase.assets

import com.intellect.logos.domain.repository.AssetsRepository

class LoadAssetsUseCase(private val assetsRepository: AssetsRepository) {

    suspend operator fun invoke(): Result<Unit> {
        return assetsRepository.loadAssets()
    }
}