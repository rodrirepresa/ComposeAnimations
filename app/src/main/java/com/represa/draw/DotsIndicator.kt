package com.represa.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.represa.draw.data.desserts
import com.represa.draw.extensions.targetValue
import com.represa.draw.ui.*

@ExperimentalPagerApi
@Composable
fun DotsIndicator() {
    var scope = rememberCoroutineScope()
    val list = remember { desserts }
    val dotSettings =
        IndicatorState.DotSettings(size = list.size, radius = 12f, color = Color.Black)
    val pagerState = rememberPagerState(pageCount = list.size)
    val state = remember { IndicatorState(scope, dotSettings) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFCFF))
    ) {

        HorizontalPager(
            state = pagerState,
            itemSpacing = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp)
        ) { page ->
            // Our page content
            DessertCard(dessert = list[page])
        }

        Indicators(
            state,
            pagerState,
            Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
        )
    }
}

@ExperimentalPagerApi
@Composable
fun Indicators(state: IndicatorState, pagerState: PagerState, modifier: Modifier) {
    if (pagerState.isScrollInProgress && state.targetPosition != pagerState.targetValue()) {
        state.startScrolling(pagerState.targetValue()!!)
    } else if (!pagerState.isScrollInProgress) {
        state.finishScrolling()
    }
    emptyIndicators(state = state, modifier = modifier)
    filledIndicators(state = state)
}


@Composable
fun emptyIndicators(state: IndicatorState, modifier: Modifier) {
    var dotSettings = state.dotSettings
    Canvas(
        modifier = modifier
    ) {
        state.setFirstIndicatorPosition(center)
        for (i in 0 until state.dotSettings.size) {
            drawCircle(
                color = dotSettings.color,
                radius = dotSettings.radius,
                center = Offset(
                    state.firstDotPosition!!.x + dotSettings.distanceBetweenDots * i,
                    state.firstDotPosition!!.y
                ),
                alpha = 0.2f
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun filledIndicators(state: IndicatorState) {
    firstFilledDot(state)
    secondFilledDot(state)
    drawUnion(state)
}
