package com.intellect.logos.common

fun String.format(
    minFraction: Int = 0,
    maxFraction: Int = 2
): String {
    require(minFraction >= 0) { "minFraction should be greater than or equal to 0" }
    require(maxFraction >= 0) { "maxFraction should be greater than or equal to 0" }
    require(minFraction <= maxFraction) { "minFraction should be less than or equal to maxFraction" }

    val parts = split('.')
    val integerPart = parts.first()
    val fractionPart = if (parts.size > 1) {
        val fractionPart = parts.last()

        when {
            fractionPart.length < minFraction -> {
                fractionPart.padEnd(minFraction, '0')
            }

            fractionPart.length > maxFraction -> {
                fractionPart.substring(0, maxFraction)
            }

            else -> {
                fractionPart
            }
        }
    } else {
        ""
    }

    val formattedIntegerPart = integerPart
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()

    return buildString {
        append(formattedIntegerPart)

        if (this@format.contains('.')) {
            append('.')
        }

        if (fractionPart.isNotEmpty()) {
            append(fractionPart)
        }
    }
}