package com.represa.draw

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.represa.draw.data.desserts
import kotlin.math.absoluteValue


@ExperimentalPagerApi
@Composable
fun DotsIndicator() {
    var scope = rememberCoroutineScope()
    val list = remember { desserts }
    var dotSettings =
        IndicatorState.DotSettings(size = list.size, radius = 12f, color = Color.Black)
    val pagerState = rememberPagerState(pageCount = list.size)
    var state = remember { IndicatorState(scope, dotSettings) }

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
            Card(
                Modifier
                    .padding(0.dp, 10.dp)
                    .width(300.dp)
                    .height(320.dp),
                elevation = 3.dp,
                shape = RoundedCornerShape(7.dp)
            ) {
                Column {
                    Image(
                        painter = rememberCoilPainter(
                            list[page].url,
                            fadeIn = true
                        ),
                        contentDescription = "f",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.height(200.dp)
                    )
                    Column(Modifier.padding(10.dp)) {
                        Text(
                            text = list[page].name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = list[page].description,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(0.dp, 10.dp),
                            color = Color.Gray
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Text(text = list[page].price)
                        }
                    }
                }
            }
        }


        Indicators(
            state,
            Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
        )

        test(state = state, pagerState = pagerState)

    }
}

@ExperimentalPagerApi
@Composable
fun test(state: IndicatorState, pagerState: PagerState) {
    //var currentValue = state.currentValue
    //state.scrollReverse(currentValue)
    if (pagerState.isScrollInProgress && state.targetPosition != pagerState.targetPage) {
        if(pagerState.targetPage!! > state.currentPosition+1 && pagerState.targetPage!! > state.currentPosition){
            state.startScrolling(state.currentPosition+1)
        }else if(pagerState.targetPage!! < state.currentPosition-1 && pagerState.targetPage!! < state.currentPosition){
            state.startScrolling(state.currentPosition-1)
        }else{
            state.startScrolling(pagerState.targetPage!!)
        }

    } else if (!pagerState.isScrollInProgress) {
        state.finishScrolling()
    }
}

@ExperimentalPagerApi
@Composable
fun Indicators(
    state: IndicatorState,
    modifier: Modifier
) {
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

    firstFilledDot(state)
    secondFilledCircle(state)
    drawUnion(state)
}

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
fun secondFilledCircle(state: IndicatorState) {
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
    } else {

    }
}


class IndicatorState @ExperimentalPagerApi constructor(
    val scope: CoroutineScope,
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
    var stateFirstDot by mutableStateOf(DotState.IDLE)
    var stateSecondDot by mutableStateOf(DotState.IDLE)

    fun startScrolling(targetValue: Int) {
        if (targetValue < dotSettings.size && currentPosition < dotSettings.size) {
            /*if(targetValue == targetPosition +2){
                scope.launch {
                    targetPosition++
                    animation.snapTo(0f)
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                    targetPosition++
                    animation.snapTo(0f)
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }
            }else if( targetValue == targetPosition -2) {
                scope.launch {
                    targetPosition--
                    animation.snapTo(0f)
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                    targetPosition--
                    animation.snapTo(0f)
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }
            }else{*/
            scope.launch {
                targetPosition = targetValue
                stateFirstDot = DotState.SCROLLING
                animation.snapTo(0f)
                animation.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                )
            }
        }
        //}
    }

    fun finishScrolling() {
        stateSecondDot = DotState.IDLE
        if (targetPosition != currentPosition) {
            scope.launch {
                stateSecondDot = DotState.SCROLLING
                animationSecond.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                )
                animationSecond.snapTo(0f)
                currentPosition = targetPosition
                stateSecondDot = DotState.IDLE
            }
        } else {
            /*scope.launch {
                targetPosition = currentPosition
                animation.snapTo(0f)
                animation.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                )
            }*/
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
