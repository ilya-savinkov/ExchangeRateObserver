package com.intellect.logos.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.intellect.logos.data.db.entity.AssetEntity

@Dao
interface AssetDao {

    @Upsert
    suspend fun upsertAll(assets: List<AssetEntity>)

    @Query("SELECT * FROM AssetEntity LIMIT :loadSize OFFSET :page * :loadSize")
    suspend fun getAll(page: Int, loadSize: Int): List<AssetEntity>

    // TODO Сделать поиск включая переводы
    @Query(
        """
        SELECT * FROM AssetEntity 
        WHERE countryCode LIKE '%' || :query || '%' 
        OR currencyCode LIKE '%' || :query || '%'
        OR countryDescription LIKE '%' || :query || '%'
        OR currencyDescription LIKE '%' || :query || '%'
        LIMIT :loadSize OFFSET :page * :loadSize
        """
    )
    suspend fun search(query: String, page: Int, loadSize: Int): List<AssetEntity>

    @Query("SELECT * FROM AssetEntity WHERE currencyCode = :name")
    suspend fun getAsset(name: String): AssetEntity

    @Query("DELETE FROM AssetEntity")
    suspend fun clearAll()
}