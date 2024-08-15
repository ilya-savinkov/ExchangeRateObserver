package com.intellect.logos.data.repository

import com.intellect.logos.data.datasource.exchange.ExchangeLocalDataSource
import com.intellect.logos.data.datasource.exchange.ExchangeRemoteDataSource
import com.intellect.logos.domain.repository.ExchangeRepository
import kotlinx.datetime.Clock
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.hours

@Single(binds = [ExchangeRepository::class])
class ExchangeRepositoryImpl(
    private val exchangeRemoteDataSource: ExchangeRemoteDataSource,
    private val exchangeLocalDataSource: ExchangeLocalDataSource
) : ExchangeRepository {
    companion object {
        private val CACHE_EXPIRATION_TIME = 1.hours
    }

    override suspend fun getRate(
        baseAssetName: String,
        quoteAssetName: String
    ): Result<Double> {
        val cachedRate = exchangeLocalDataSource.getRate(
            baseAssetName = baseAssetName,
            quoteAssetName = quoteAssetName
        )

        return if (cachedRate != null) {
            return if (cachedRate.cachedTime.plus(CACHE_EXPIRATION_TIME) < Clock.System.now()) {
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
        ).onSuccess { rate ->
            exchangeLocalDataSource.saveRate(
                baseAssetName = baseAssetName,
                quoteAssetName = quoteAssetName,
                rate = rate,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }
    }
}