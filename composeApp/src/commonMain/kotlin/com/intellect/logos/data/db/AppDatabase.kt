@file:Suppress("EXPECT_AND_ACTUAL_IN_THE_SAME_MODULE")

package com.intellect.logos.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.intellect.logos.data.db.dao.AssetDao
import com.intellect.logos.data.db.dao.RateDao
import com.intellect.logos.data.db.entity.AssetEntity
import com.intellect.logos.data.db.entity.RateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

expect object MyDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

@Database(
    entities = [
        AssetEntity::class,
        RateEntity::class
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(MyDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao
    abstract fun rateDao(): RateDao
}

fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}