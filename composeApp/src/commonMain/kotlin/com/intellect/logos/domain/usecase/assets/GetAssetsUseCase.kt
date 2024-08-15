package com.intellect.logos.domain.usecase.assets

import androidx.paging.PagingData
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetAssetsUseCase(private val assetsRepository: AssetsRepository) {

    operator fun invoke(): Flow<PagingData<Asset>> = assetsRepository.getAssets()
}