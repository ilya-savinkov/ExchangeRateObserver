package com.intellect.logos.data.model

import kotlinx.datetime.Instant

class CachedRate(
    val rate: Double,
    val cachedTime: Instant
)