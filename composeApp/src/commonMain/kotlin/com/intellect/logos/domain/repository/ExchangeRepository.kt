package com.intellect.logos.domain.repository

import com.intellect.logos.domain.model.exchange.Volume
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    fun getVolumeFlow(): Flow<Volume>
    suspend fun cacheVolume(volume: Volume)
    suspend fun getRate(baseAssetName: String, quoteAssetName: String): Result<Double>
}