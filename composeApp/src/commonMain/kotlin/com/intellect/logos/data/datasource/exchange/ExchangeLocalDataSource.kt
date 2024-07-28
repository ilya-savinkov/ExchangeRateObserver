package com.intellect.logos.data.datasource.exchange

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExchangeLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {
    private val volumeKey: Preferences.Key<String> = stringPreferencesKey("volume")

    fun getVolume(): Flow<String> {
        return dataStore.data.map {
            it[volumeKey] ?: "1"
        }
    }

    suspend fun cacheVolume(volume: String) {
        dataStore.edit {
            it[volumeKey] = volume
        }
    }
}