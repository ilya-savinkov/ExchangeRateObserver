package com.intellect.logos.domain.usecase.volume

import com.intellect.logos.domain.model.exchange.Volume
import com.intellect.logos.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.core.annotation.Factory

@Factory
class GetVolumeFlowUseCase(private val exchangeRepository: ExchangeRepository) {

    operator fun invoke(): Flow<Volume> =
        exchangeRepository.getVolumeFlow()
            .distinctUntilChanged()
}