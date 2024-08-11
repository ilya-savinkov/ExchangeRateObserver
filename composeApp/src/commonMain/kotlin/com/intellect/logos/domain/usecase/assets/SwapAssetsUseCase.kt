package com.intellect.logos.domain.usecase.assets

import com.intellect.logos.domain.repository.AssetsRepository

class SwapAssetsUseCase(private val assetsRepository: AssetsRepository) {

    suspend operator fun invoke() {
        assetsRepository.swap()
    }
}