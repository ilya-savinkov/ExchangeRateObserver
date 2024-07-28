package com.intellect.logos.data.mapper

import ServerConstant
import com.intellect.logos.data.db.entity.AssetEntity
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.Country
import com.intellect.logos.domain.model.Currency
import io.ktor.http.encodeURLPathPart
import response.AssetResponse

fun List<AssetResponse>.toEntities(): List<AssetEntity> = map(AssetResponse::toEntity)

fun AssetResponse.toEntity(): AssetEntity = AssetEntity(
    currencyCode = currency.code,
    currencyDescription = currency.description.getValue("en"),
    countryCode = country.code,
    countryDescription = country.description.getValue("en"),
    icon = "${ServerConstant.URL}/${icon}".encodeURLPathPart()
)

fun AssetEntity.toDomain(): Asset = Asset(
    icon = icon,
    currency = Currency(
        code = currencyCode,
        description = currencyDescription
    ),
    country = Country(
        code = countryCode,
        description = countryDescription
    )
)