package com.represa.draw.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.represa.draw.data.desserts

@ExperimentalPagerApi
@Composable
fun RoundIndicators() {
    val list = remember { desserts }
    val dotSettings =
        NewIndicatorState.NewDotSettings(size = list.size, radius = 12f)
    val pagerState = rememberPagerState(pageCount = list.size)
    val state = remember { NewIndicatorState(dotSettings) }

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

        NewIndicators(
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
fun NewIndicators(state: NewIndicatorState, pagerState: PagerState, modifier: Modifier) {
    drawIndicators(state = state, modifier = modifier)
    if (pagerState.currentPage != state.currentDot) {
        state.currentDot = pagerState.currentPage
    }
}


@Composable
fun drawIndicators(state: NewIndicatorState, modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Canvas(modifier = modifier) {
            state.setFirstIndicatorPosition(center)
        }
        for (i in 0 until state.dotSettings.size) {
            singleRoundIndicator(state = state, position = i)
        }
    }
}

@Composable
fun singleRoundIndicator(state: NewIndicatorState, position: Int) {
    var dotSettings = state.dotSettings
    val alpha: Float by animateFloatAsState(
        if (state.currentDot == position) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )
    val size: Float by animateFloatAsState(
        if (state.currentDot == position) 1.5f else 0.9f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )
    val strokeColor: Color by animateColorAsState(if (state.currentDot == position) dotSettings.strokeColorSelected else dotSettings.strokeColor)
    val fillColor: Color by animateColorAsState(if (state.currentDot == position) dotSettings.fillColorSelected else dotSettings.fillColor)
    Box(modifier = Modifier.drawBehind {
        drawCircle(
            color = strokeColor,
            radius = dotSettings.radius * 1.2f * size,
            center = Offset(
                state.firstDotPosition!!.x + dotSettings.distanceBetweenDots * position,
                state.firstDotPosition!!.y
            ),
            alpha = alpha
        )
        drawCircle(
            color = fillColor,
            radius = dotSettings.radius * size,
            center = Offset(
                state.firstDotPosition!!.x + dotSettings.distanceBetweenDots * position,
                state.firstDotPosition!!.y
            )
        )
    }) {
    }
}

class NewIndicatorState @ExperimentalPagerApi constructor(
    val dotSettings: NewDotSettings
) {

    var firstDotPosition: Offset? = null

    var currentDot by mutableStateOf(0)

    fun setFirstIndicatorPosition(center: Offset) {
        firstDotPosition = Offset(
            center.x - (((dotSettings.size - 1) * dotSettings.distanceBetweenDots) / 2),
            center.y
        )
    }

    class NewDotSettings(
        var size: Int,
        var radius: Float,
        var distanceBetweenDots: Float = radius * 5,
        var strokeColor: Color = Color.Gray,
        var fillColor: Color = Color.White,
        var strokeColorSelected: Color = Color.White,
        var fillColorSelected: Color = Color.Black
    )
}
