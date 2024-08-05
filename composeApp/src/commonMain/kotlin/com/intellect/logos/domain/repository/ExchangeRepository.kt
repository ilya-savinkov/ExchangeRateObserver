package com.intellect.logos.domain.repository

import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    fun getVolume(): Flow<String>
    suspend fun cacheVolume(volume: String)
    suspend fun getRate(from: String, to: String): Result<Double>
}