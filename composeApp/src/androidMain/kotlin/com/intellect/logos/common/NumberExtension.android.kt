package com.intellect.logos.common

import android.icu.text.DecimalFormat

actual fun Double.format(digits: Int): String {
    return DecimalFormat().apply {
        minimumFractionDigits = digits
        maximumFractionDigits = digits
    }.format(this@format)
}