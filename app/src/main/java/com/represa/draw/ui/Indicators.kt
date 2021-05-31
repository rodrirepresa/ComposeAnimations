package com.represa.draw.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@ExperimentalPagerApi
@Composable
fun firstFilledDot(state: IndicatorState) {
    if (state.stateFirstDot == IndicatorState.DotState.SCROLLING) {
        Canvas(
            Modifier.fillMaxWidth()
        ) {
            var targetPosition = state.getTargetValue(state.targetPosition)
            var currentPosition = state.getTargetValue(state.currentPosition)
            var distanceFirstCircle = (targetPosition.x - currentPosition.x)

            //This is gonna be the first filled dot
            var firstDotAnimated = Offset(
                currentPosition.x + (distanceFirstCircle * state.animation.value),
                state.firstDotPosition!!.y
            )
            drawCircle(
                color = Color.Black,
                radius = state.dotSettings.radius,
                center = firstDotAnimated,
                alpha = 0.8f
            )
        }
    } else {
        Canvas(
            Modifier.fillMaxWidth()
        ) {
            var currentPosition = state.getTargetValue(state.currentPosition)

            //This is gonna be the first filled dot
            var firstDotAnimated = Offset(
                currentPosition.x,
                state.firstDotPosition!!.y
            )
            drawCircle(
                color = Color.Black,
                radius = state.dotSettings.radius,
                center = firstDotAnimated,
                alpha = 0.8f
            )
        }
    }
}

@Composable
fun secondFilledDot(state: IndicatorState) {
    if (state.stateSecondDot == IndicatorState.DotState.SCROLLING) {

        Canvas(
            Modifier.fillMaxWidth()
        ) {
            var targetPosition = state.getTargetValue(state.targetPosition)
            var currentPosition = state.getTargetValue(state.currentPosition)
            var distanceFirstCircle = (targetPosition.x - currentPosition.x)

            //This is gonna be the first filled dot
            var secondDotAnimated = Offset(
                currentPosition.x + (distanceFirstCircle * state.animationSecond.value),
                state.firstDotPosition!!.y
            )
            drawCircle(
                color = Color.Black,
                radius = state.dotSettings.radius,
                center = secondDotAnimated,
                alpha = 0.8f
            )
        }
    } else {
        Canvas(
            Modifier.fillMaxWidth()
        ) {
            var currentPosition = state.getTargetValue(state.currentPosition)

            //This is gonna be the first filled dot
            var secondDotAnimated = Offset(
                currentPosition.x,
                state.firstDotPosition!!.y
            )
            drawCircle(
                color = Color.Black,
                radius = state.dotSettings.radius,
                center = secondDotAnimated,
                alpha = 0.8f
            )
        }
    }
}

@Composable
fun drawUnion(state: IndicatorState) {
    if (state.stateFirstDot == IndicatorState.DotState.SCROLLING) {
        Canvas(
            Modifier.fillMaxWidth()
        ) {
            var targetPosition = state.getTargetValue(state.targetPosition)
            var currentPosition = state.getTargetValue(state.currentPosition)
            var distanceFirstCircle = (targetPosition.x - currentPosition.x)

            var firstDotAnimated = Offset(
                currentPosition.x + (distanceFirstCircle * state.animation.value),
                state.firstDotPosition!!.y
            )
            var secondDotAnimated = Offset(
                currentPosition.x + (distanceFirstCircle * state.animationSecond.value),
                state.firstDotPosition!!.y
            )
            //This gonna be the rectangle between filled dots
            var topleft = if (secondDotAnimated.x <= firstDotAnimated.x) {
                Offset(
                    secondDotAnimated.x,
                    state.firstDotPosition!!.y - state.dotSettings.radius
                )
            } else {
                Offset(
                    firstDotAnimated.x,
                    state.firstDotPosition!!.y - state.dotSettings.radius
                )
            }
            drawRect(
                color = Color.Black,
                alpha = 0.8f,
                topLeft = topleft,
                size = Size(
                    (secondDotAnimated.x - firstDotAnimated.x).absoluteValue,
                    state.dotSettings.radius * 2
                )
            )
        }
    }
}


class IndicatorState @ExperimentalPagerApi constructor(
    private val scope: CoroutineScope,
    val dotSettings: DotSettings
) {

    enum class DotState {
        IDLE, SCROLLING
    }

    var firstDotPosition: Offset? = null
    var animation = Animatable(initialValue = 0f)
    var animationSecond = Animatable(initialValue = 0f)

    var currentPosition = 0
    var targetPosition = 0
    var isScrollingBack = false
    var stateFirstDot by mutableStateOf(DotState.IDLE)
    var stateSecondDot by mutableStateOf(DotState.IDLE)

    fun startScrolling(targetValue: Int) {
        if (targetValue < dotSettings.size && currentPosition < dotSettings.size) {
            if (targetValue != currentPosition) {
                scope.launch {
                    targetPosition = targetValue
                    stateFirstDot = DotState.SCROLLING
                    animation.snapTo(0f)
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }
            } else if (!isScrollingBack) {
                isScrollingBack = true
                scope.launch {
                    stateFirstDot = DotState.SCROLLING
                    animation.snapTo(1f)
                    animation.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                    targetPosition = targetValue
                    isScrollingBack = false
                }
            }
        }
    }

    fun finishScrolling() {
        stateSecondDot = DotState.IDLE
        if (targetPosition != currentPosition) {
            scope.launch {
                stateSecondDot = DotState.SCROLLING
                animationSecond.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                )
                animationSecond.snapTo(0f)
                currentPosition = targetPosition
                stateSecondDot = DotState.IDLE
            }
        }
    }

    fun setFirstIndicatorPosition(center: Offset) {
        firstDotPosition = Offset(
            center.x - (((dotSettings.size - 1) * dotSettings.distanceBetweenDots) / 2),
            center.y
        )
    }

    fun getTargetValue(target: Int) = Offset(
        firstDotPosition!!.x + dotSettings.distanceBetweenDots * target,
        firstDotPosition!!.y
    )

    class DotSettings(
        var size: Int,
        var radius: Float,
        var distanceBetweenDots: Float = radius * 5,
        var color: Color = Color.White
    )

}