package com.intellect.logos.data.mapper

import com.intellect.logos.data.db.entity.AssetEntity
import com.intellect.logos.domain.model.Asset
import response.AssetResponse

fun List<AssetResponse>.toEntities(): List<AssetEntity> = map(AssetResponse::toEntity)

fun AssetResponse.toEntity(): AssetEntity = AssetEntity(
    name = name,
    description = description,
    icon = icon
)

fun AssetResponse.toDomain(): Asset = Asset(
    name = name,
    description = description,
    icon = icon
)

fun AssetEntity.toDomain(): Asset = Asset(
    name = name,
    description = description,
    icon = icon
)