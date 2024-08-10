package com.intellect.logos.domain.usecase.assets

import androidx.paging.PagingData
import androidx.paging.filter
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.repository.AssetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAssetsUseCase(private val assetsRepository: AssetsRepository) {
    suspend operator fun invoke(
        query: String,
        exclude: String
    ): Flow<PagingData<Asset>> {
        return assetsRepository.getAssets(query.trim()).map { pagingSource ->
            pagingSource.filter { asset ->
                asset.currency.code != exclude
            }
        }
    }
}