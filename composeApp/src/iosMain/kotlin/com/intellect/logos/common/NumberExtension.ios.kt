package com.intellect.logos.common

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

actual fun Double.format(digits: Int): String {
    return NSNumberFormatter().apply {
        minimumFractionDigits = 0u
        maximumFractionDigits = 2u
    }.stringFromNumber(NSNumber(this)).orEmpty()
}