package com.intellect.logos.domain.usecase.volume

import com.intellect.logos.domain.model.Currency
import com.intellect.logos.domain.model.Key
import com.intellect.logos.domain.repository.ExchangeRepository

class CalculateVolumeUseCase(private val exchangeRepository: ExchangeRepository) {

    suspend operator fun invoke(
        volume: String,
        key: Key,
        currency: Currency
    ) {
        val prevVolume = if (volume.startsWith("0.")) {
            volume
        } else {
            volume.trimStart('0')
        }

        val newVolume = when (key) {
            Key.Backspace -> prevVolume.dropLast(1)

            is Key.Number -> if (prevVolume.contains('.')) {
                val decimal = prevVolume.split('.').last().length

                if (decimal == 2) {
                    prevVolume
                } else {
                    prevVolume + key.value.toString()
                }
            } else {
                prevVolume + key.value.toString()
            }

            Key.Dot -> if (prevVolume.contains('.') || currency.isJPY) {
                prevVolume
            } else {
                if (prevVolume.isEmpty()) {
                    "0."
                } else {
                    "$prevVolume."
                }
            }
        }.take(10).ifEmpty { "0" }

        exchangeRepository.cacheVolume(newVolume)
    }
}