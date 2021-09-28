package com.represa.draw.extensions

import androidx.compose.ui.unit.Dp
import kotlin.math.absoluteValue

fun Float.notOvertaking(maxValue: Float): Float {
    return this.absoluteValue.takeIf { it < maxValue } ?: maxValue
}

fun Float.notLower(lowerValue: Float): Float {
    return this.takeIf { it > lowerValue } ?: lowerValue
}

fun Dp.notLower(lowerValue: Dp): Dp {
    return this.takeIf { it > lowerValue } ?: lowerValue
}