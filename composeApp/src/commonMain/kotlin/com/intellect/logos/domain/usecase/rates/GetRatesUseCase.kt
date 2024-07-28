package com.intellect.logos.domain.usecase.rates

import com.intellect.logos.domain.repository.ExchangeRepository

class GetRatesUseCase(private val exchangeRepository: ExchangeRepository) {

    suspend operator fun invoke(from: String, to: String): Result<Double> {
        return Result.success(1.2)
    }
}