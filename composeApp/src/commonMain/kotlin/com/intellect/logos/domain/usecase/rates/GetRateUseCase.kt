package com.intellect.logos.domain.usecase.rates

import com.intellect.logos.domain.repository.ExchangeRepository

class GetRateUseCase(private val exchangeRepository: ExchangeRepository) {

    suspend operator fun invoke(
        baseAssetName: String,
        quoteAssetName: String
    ): Result<Double> = exchangeRepository.getRate(
        baseAssetName = baseAssetName,
        quoteAssetName = quoteAssetName
    )
}