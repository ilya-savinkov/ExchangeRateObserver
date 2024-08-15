package com.intellect.logos.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.intellect.logos.data.db.entity.AssetEntity

@Dao
interface AssetDao {

    @Upsert
    suspend fun upsert(assetEntity: AssetEntity)

    @Upsert
    suspend fun upsertAll(assets: List<AssetEntity>)

    @Query("SELECT * FROM AssetEntity WHERE name = :name")
    suspend fun getAsset(name: String): AssetEntity?

    @Query("SELECT * FROM AssetEntity")
    fun getAssets(): PagingSource<Int, AssetEntity>

    @Query("DELETE FROM AssetEntity")
    suspend fun clearAll()
}