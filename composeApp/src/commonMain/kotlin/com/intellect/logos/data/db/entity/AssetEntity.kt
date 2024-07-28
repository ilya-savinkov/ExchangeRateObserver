package com.intellect.logos.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AssetEntity(
    @PrimaryKey val currencyCode: String,
    val currencyDescription: String,
    val countryCode: String,
    val countryDescription: String,
    val icon: String,
)