package com.intellect.logos.data.datasource.exchange

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.intellect.logos.data.db.dao.RateDao
import com.intellect.logos.data.db.entity.RateEntity
import com.intellect.logos.domain.model.exchange.Volume
import com.intellect.logos.domain.model.exchange.default
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class ExchangeLocalDataSource(
    @Provided private val rateDao: RateDao,
    @Provided private val dataStore: DataStore<Preferences>,
    @Provided private val json: Json
) {
    private val volumeKey: Preferences.Key<String> = stringPreferencesKey("volume")

    fun getVolumeFlow(): Flow<Volume> {
        return dataStore.data.map {
            it[volumeKey]?.let(json::decodeFromString) ?: Volume.default()
        }
    }

    suspend fun cacheVolume(volume: Volume) {
        dataStore.edit {
            it[volumeKey] = json.encodeToString(volume)
        }
    }

    suspend fun updateRate(base: String, quote: String, rate: Double) {
        rateDao.upsert(
            RateEntity(
                base = base,
                quote = quote,
                rate = rate,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        )
    }

    suspend fun getRate(base: String, quote: String): RateEntity? {
        return rateDao.getRate(
            base = base,
            quote = quote
        )
    }
}