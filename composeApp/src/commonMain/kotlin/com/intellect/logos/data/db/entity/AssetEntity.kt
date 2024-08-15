package com.intellect.logos.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AssetEntity(
    val name: String,
    val description: String,
    val icon: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}