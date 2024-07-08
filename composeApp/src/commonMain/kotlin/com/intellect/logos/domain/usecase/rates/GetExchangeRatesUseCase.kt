package com.intellect.logos.domain.usecase.rates

import com.intellect.logos.domain.repository.ExchangeRatesRepository

class GetExchangeRatesUseCase(private val exchangeRatesRepository: ExchangeRatesRepository) {
    suspend operator fun invoke() {

    }
}