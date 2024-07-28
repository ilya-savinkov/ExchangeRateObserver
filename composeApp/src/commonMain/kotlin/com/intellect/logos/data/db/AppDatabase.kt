package com.intellect.logos.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.intellect.logos.data.db.dao.AssetDao
import com.intellect.logos.data.db.entity.AssetEntity

@Database(
    entities = [AssetEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(), DB {

    abstract fun assetDao(): AssetDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

// FIXME: Added a hack to resolve below issue:
// Class 'AppDatabase_Impl' is not abstract and does not implement abstract base class member 'clearAllTables'.
private interface DB {
    fun clearAllTables() {}
}