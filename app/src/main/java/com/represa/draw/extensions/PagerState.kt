package com.represa.draw.extensions

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

@ExperimentalPagerApi
fun PagerState.targetValue(): Int {
    return if (this.currentPageOffset in -0.15..0.15) {
        currentPage
    } else {
        this.targetPage!!
    }
}