package com.intellect.logos.domain.usecase.volume

import com.intellect.logos.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class GetVolumeUseCase(private val exchangeRepository: ExchangeRepository) {

    operator fun invoke(): Flow<String> =
        exchangeRepository.getVolume()
            .distinctUntilChanged()
}