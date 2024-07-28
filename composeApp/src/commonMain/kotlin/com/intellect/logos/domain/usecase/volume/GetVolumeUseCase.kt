package com.intellect.logos.domain.usecase.volume

import com.intellect.logos.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow

class GetVolumeUseCase(private val exchangeRepository: ExchangeRepository) {

    operator fun invoke(): Flow<String> = exchangeRepository.getVolume()
}