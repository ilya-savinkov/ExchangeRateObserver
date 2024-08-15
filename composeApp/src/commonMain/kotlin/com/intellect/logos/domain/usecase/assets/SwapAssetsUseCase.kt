package com.intellect.logos.domain.usecase.assets

import com.intellect.logos.domain.repository.AssetsRepository
import org.koin.core.annotation.Factory

@Factory
class SwapAssetsUseCase(private val assetsRepository: AssetsRepository) {

    suspend operator fun invoke() {
        assetsRepository.swap()
    }
}