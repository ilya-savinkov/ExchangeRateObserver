package com.intellect.logos.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RateEntity(
    val base: String,
    val quote: String,
    val rate: Double,
    val timestamp: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}