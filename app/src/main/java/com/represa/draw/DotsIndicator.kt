package com.represa.draw

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.geometry.Size
import kotlin.math.absoluteValue


@Composable
fun DotsIndicator() {
    var scope = rememberCoroutineScope()
    var dotSettings = IndicatorState.DotSettings(size = 5, radius = 15f, color = Color.Black)
    var state = remember { IndicatorState(scope, dotSettings) }
    Column(
        Modifier
            .fillMaxSize()
    ) {
        Row() {
            Button(
                onClick = { state.scroll() },
                enabled = state.scrollEnabled()
            ) {
                Text(text = "scroll")
            }
            Button(
                onClick = { state.scrollReverse() },
                enabled = state.reverseScrollEnabled()
            ) {
                Text(text = "scroll reverse")
            }
        }

        Indicators(state)
    }
}


@Composable
fun Indicators(
    state: IndicatorState
) {
    var dotSettings = state.dotSettings
    Canvas(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 20.dp, 0.dp, 0.dp)
    ) {
        state.setFirstDotPosition(center)
        for (i in 0 until state.dotSettings.size) {
            drawCircle(
                color = dotSettings.color,
                radius = dotSettings.radius,
                center = Offset(
                    state.firstDotPosition!!.x + dotSettings.distanceBetweenDots * i,
                    state.firstDotPosition!!.y
                ),
                alpha = 0.5f
            )
        }
    }

    filledDot(state)
}

@Composable
fun filledDot(state: IndicatorState) {
    var dotSettings = state.dotSettings
    Canvas(
        Modifier.fillMaxWidth()
    ) {
        var currentItem =
            (state.firstDotPosition!!.x + dotSettings.distanceBetweenDots * state.currentItem)
        var nextItem =
            (state.firstDotPosition!!.x + dotSettings.distanceBetweenDots * state.nextItem)
        var distance = nextItem - currentItem
        //This is gonna be the first dot
        drawCircle(
            color = Color.Black,
            radius = 15f,
            center = Offset(
                currentItem + (distance * state.animation.value),
                state.firstDotPosition!!.y
            ),
            alpha = 1f
        )
        //This is gonna be the second one
        drawCircle(
            color = Color.Black,
            radius = 15f,
            center = Offset(
                currentItem + (distance * state.animationSecond.value),
                state.firstDotPosition!!.y
            ),
            alpha = 1f
        )
        //This gonna be the rectangle between dots
        var topleft = Offset(
            currentItem + (distance * state.animationSecond.value),
            state.firstDotPosition!!.y - state.dotSettings.radius
        )
        var firstDot = Offset(
            currentItem + (distance * state.animation.value),
            state.firstDotPosition!!.y
        )
        var secondDot = Offset(
            currentItem + (distance * state.animationSecond.value),
            state.firstDotPosition!!.y
        )
        drawRect(
            color = Color.Black,
            topLeft = topleft,
            size = Size( (secondDot.x - firstDot.x ).absoluteValue, state.dotSettings.radius * 2)
        )
    }
}


class IndicatorState(private val scope: CoroutineScope, val dotSettings: DotSettings) {
    var currentItem by mutableStateOf(0)
    var nextItem by mutableStateOf(1)
    var firstDotPosition: Offset? = null
    var animation = Animatable(initialValue = 0f)
    var animationSecond = Animatable(initialValue = 0f)
    private var isScrolling by mutableStateOf(false)

    fun scroll() {
        if (nextItem <= 4) {
            isScrolling = true
            scope.launch {
                nextItem = currentItem + 1
                launch {
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 175, easing = LinearEasing)
                    )
                }.join()
                launch {
                    animationSecond.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 175, easing = LinearEasing)
                    )
                }.join()
                animation.snapTo(0f)
                animationSecond.snapTo(0f)
                currentItem++
                nextItem++
                isScrolling = false
            }
        }
    }

    fun scrollReverse() {
        if (currentItem > 0) {
            isScrolling = true
            scope.launch {
                nextItem = currentItem - 1
                launch {
                    animationSecond.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 175, easing = LinearEasing)
                    )
                }.join()
                launch {
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 175, easing = LinearEasing)
                    )
                }.join()
                animation.snapTo(0f)
                animationSecond.snapTo(0f)
                currentItem--
                isScrolling = false
            }
        }
    }

    fun setFirstDotPosition(center: Offset) {
        firstDotPosition = Offset(
            center.x - (((dotSettings.size - 1) * dotSettings.distanceBetweenDots) / 2),
            center.y
        )
    }

    fun scrollEnabled() = nextItem < 5 && !isScrolling
    fun reverseScrollEnabled() = currentItem > 0 && !isScrolling

    class DotSettings(
        var size: Int,
        var radius: Float,
        var distanceBetweenDots: Float = radius * 7,
        var color: Color = Color.White
    )

}