package com.intellect.logos.data.datasource.exchange

import com.intellect.logos.data.database.table.RateTable
import com.intellect.logos.data.model.CachedRate
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single

@Single
class ExchangeLocalDataSource {

    fun getRate(
        baseAssetName: String,
        quoteAssetName: String
    ): CachedRate? {
        return transaction {
            RateTable
                .selectAll()
                .where { RateTable.baseAssetName eq baseAssetName }
                .andWhere { RateTable.quoteAssetName eq quoteAssetName }
                .map {
                    CachedRate(
                        rate = it[RateTable.rate],
                        cachedTime = Instant.fromEpochMilliseconds(it[RateTable.timestamp])
                    )
                }
                .firstOrNull()
        }
    }

    fun saveRate(
        baseAssetName: String,
        quoteAssetName: String,
        rate: Double,
        timestamp: Long
    ) {
        transaction {
            RateTable.insert {
                it[RateTable.baseAssetName] = baseAssetName
                it[RateTable.quoteAssetName] = quoteAssetName
                it[RateTable.rate] = rate
                it[RateTable.timestamp] = timestamp
            }
        }
    }
}