package com.represa.draw.util

import androidx.compose.foundation.lazy.LazyListState
import kotlin.math.absoluteValue

fun LazyListState.getCurrentItem(): Int {
    var currentOffset = layoutInfo.viewportEndOffset
    var position = -1
    layoutInfo.visibleItemsInfo.forEach {
        if (it.offset.absoluteValue < currentOffset) {
            currentOffset = it.offset.absoluteValue
            position = it.index
        }
    }
    return position
}