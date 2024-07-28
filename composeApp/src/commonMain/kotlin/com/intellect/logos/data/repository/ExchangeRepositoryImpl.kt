package com.intellect.logos.data.repository

import com.intellect.logos.data.datasource.exchange.ExchangeLocalDataSource
import com.intellect.logos.data.datasource.exchange.ExchangeRemoteDataSource
import com.intellect.logos.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow

class ExchangeRepositoryImpl(
    private val exchangeLocalDataSource: ExchangeLocalDataSource,
    private val exchangeRemoteDataSource: ExchangeRemoteDataSource
) : ExchangeRepository {

    override fun getVolume(): Flow<String> {
        return exchangeLocalDataSource.getVolume()
    }

    override suspend fun cacheVolume(volume: String) {
        exchangeLocalDataSource.cacheVolume(volume)
    }
}