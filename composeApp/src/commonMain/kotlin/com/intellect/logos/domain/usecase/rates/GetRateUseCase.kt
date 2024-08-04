package com.intellect.logos.domain.usecase.rates

import com.intellect.logos.domain.repository.ExchangeRepository

class GetRateUseCase(private val exchangeRepository: ExchangeRepository) {

    suspend operator fun invoke(from: String, to: String): Result<Double> {
        return exchangeRepository.getRate(
            from = from,
            to = to
        )
    }
}