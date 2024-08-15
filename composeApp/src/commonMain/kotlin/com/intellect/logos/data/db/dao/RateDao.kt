package com.intellect.logos.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.intellect.logos.data.db.entity.RateEntity

@Dao
interface RateDao {

    @Upsert
    suspend fun upsert(rateEntity: RateEntity)

    @Query("SELECT * FROM RateEntity WHERE base = :base AND quote = :quote")
    suspend fun getRate(base: String, quote: String): RateEntity?

    @Query("DELETE FROM RateEntity")
    suspend fun clearAll()
}