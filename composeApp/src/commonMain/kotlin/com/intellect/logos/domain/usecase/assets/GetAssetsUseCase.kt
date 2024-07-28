package com.intellect.logos.domain.usecase.assets

import androidx.paging.PagingData
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository
import kotlinx.coroutines.flow.Flow

class GetAssetsUseCase(private val assetsRepository: AssetsRepository) {
    suspend operator fun invoke(query: String): Flow<PagingData<Asset>> {
        return assetsRepository.getAssets(query.trim())
    }
}