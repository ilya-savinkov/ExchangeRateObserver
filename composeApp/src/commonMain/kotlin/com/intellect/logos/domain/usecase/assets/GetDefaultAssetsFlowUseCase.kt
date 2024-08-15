package com.intellect.logos.domain.usecase.assets

import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetDefaultAssetsFlowUseCase(private val assetsRepository: AssetsRepository) {

    suspend operator fun invoke(): Flow<Result<Pair<Asset, Asset>>> {
        return assetsRepository.getDefaultAssetsFlow()
    }
}