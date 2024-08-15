package com.intellect.logos.data.repository

import com.intellect.logos.data.datasource.exchange.ExchangeLocalDataSource
import com.intellect.logos.data.datasource.exchange.ExchangeRemoteDataSource
import com.intellect.logos.domain.model.exchange.Volume
import com.intellect.logos.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.hours

@Single(binds = [ExchangeRepository::class])
class ExchangeRepositoryImpl(
    private val exchangeLocalDataSource: ExchangeLocalDataSource,
    private val exchangeRemoteDataSource: ExchangeRemoteDataSource
) : ExchangeRepository {
    companion object {
        private val CACHE_EXPIRATION_TIME = 1.hours
    }

    override fun getVolumeFlow(): Flow<Volume> {
        return exchangeLocalDataSource.getVolumeFlow()
    }

    override suspend fun cacheVolume(volume: Volume) {
        exchangeLocalDataSource.cacheVolume(volume)
    }

    override suspend fun getRate(
        baseAssetName: String,
        quoteAssetName: String
    ): Result<Double> {
        val cachedRate = exchangeLocalDataSource.getRate(
            base = baseAssetName,
            quote = quoteAssetName
        )

        return if (cachedRate != null) {
            val cachedTime = Instant.fromEpochMilliseconds(cachedRate.timestamp)
                .plus(CACHE_EXPIRATION_TIME)

            if (cachedTime < Clock.System.now()) {
                updateRate(
                    baseAssetName = baseAssetName,
                    quoteAssetName = quoteAssetName
                )
            } else {
                Result.success(cachedRate.rate)
            }
        } else {
            updateRate(
                baseAssetName = baseAssetName,
                quoteAssetName = quoteAssetName
            )
        }
    }

    private suspend fun updateRate(
        baseAssetName: String,
        quoteAssetName: String
    ): Result<Double> {
        return exchangeRemoteDataSource.getRate(
            baseAssetName = baseAssetName,
            quoteAssetName = quoteAssetName
        ).map {
            it.rate
        }.onSuccess { rate ->
            exchangeLocalDataSource.updateRate(
                base = baseAssetName,
                quote = quoteAssetName,
                rate = rate
            )
        }
    }
}