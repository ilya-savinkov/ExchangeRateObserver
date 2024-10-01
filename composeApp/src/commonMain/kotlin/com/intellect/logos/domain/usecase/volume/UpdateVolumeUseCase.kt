package com.intellect.logos.domain.usecase.volume

import com.intellect.logos.common.format
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.exchange.Key
import com.intellect.logos.domain.model.exchange.Volume
import com.intellect.logos.domain.repository.ExchangeRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateVolumeUseCase(private val exchangeRepository: ExchangeRepository) {
    companion object {
        const val MAX_VOLUME_LENGTH = 10
    }

    suspend operator fun invoke(
        asset: Asset,
        volume: Volume,
        key: Key
    ) {
        val volumeText = volume.text.replace(
            oldValue = ",",
            newValue = ""
        )

        val prevVolume = if (volumeText.startsWith("0.")) {
            volumeText
        } else {
            volumeText.trimStart('0')
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

            Key.Dot -> if (prevVolume.contains('.') || asset.isJPY) {
                prevVolume
            } else {
                if (prevVolume.isEmpty()) {
                    "0."
                } else {
                    "$prevVolume."
                }
            }
        }.take(MAX_VOLUME_LENGTH)
            .ifEmpty { "0" }
            .toDouble()

        exchangeRepository.cacheVolume(
            volume.copy(
                value = newVolume,
                text = newVolume.format()
            )
        )
    }
}