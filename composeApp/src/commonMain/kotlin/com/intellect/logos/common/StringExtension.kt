package com.intellect.logos.common

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

fun Double.format(
    minFraction: Int = 0,
    maxFraction: Int = 2,
    isGroupingUsed: Boolean = true
): String {
    require(minFraction >= 0) { "minFraction must be greater than or equal to 0" }
    require(maxFraction >= 0) { "maxFraction must be greater than or equal to 0" }
    require(minFraction <= maxFraction) { "minFraction must be less than or equal to maxFraction" }

    // Округляем число до необходимого количества знаков после запятой
    val scaleFactor = 10.0.pow(maxFraction)
    val roundedValue = (this * scaleFactor).roundToLong() / scaleFactor

    // Разбиваем на целую и дробную части
    val integerPart = roundedValue.toLong()
    val fractionPart = (abs(roundedValue - integerPart) * scaleFactor).roundToLong()

    // Форматируем дробную часть
    var fractionString = fractionPart.toString().padStart(maxFraction, '0')

    // Обрезаем или дополняем дробную часть в соответствии с minFraction и maxFraction
    if (fractionString.length > maxFraction) {
        fractionString = fractionString.substring(0, maxFraction)
    }

    // Удаляем лишние нули в конце, если они превышают minFraction
    fractionString = fractionString.trimEnd('0')
    if (fractionString.length < minFraction) {
        fractionString = fractionString.padEnd(minFraction, '0')
    }

    // Форматируем целую часть с разделителями, если необходимо
    val integerString = if (isGroupingUsed) {
        integerPart.toString()
            .reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()
    } else {
        integerPart.toString()
    }

    return if (fractionString.isNotEmpty()) {
        "$integerString.$fractionString"
    } else {
        integerString
    }
}