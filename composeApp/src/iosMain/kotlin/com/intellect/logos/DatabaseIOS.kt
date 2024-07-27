package com.intellect.logos

import androidx.room.Room
import androidx.room.RoomDatabase
import com.intellect.logos.data.db.AppDatabase
import com.intellect.logos.data.db.instantiateImpl
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/app.db"

    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory = { AppDatabase::class.instantiateImpl() }
    )
}