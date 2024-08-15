package com.intellect.logos.domain.usecase.exchange

import com.intellect.logos.domain.repository.ExchangeRepository
import org.koin.core.annotation.Factory

@Factory
class GetRateUseCase(private val exchangeRepository: ExchangeRepository) {

    suspend operator fun invoke(baseAssetName: String, quoteAssetName: String): Result<Double> {
        return exchangeRepository.getRate(
            baseAssetName = baseAssetName,
            quoteAssetName = quoteAssetName
        )
    }
}