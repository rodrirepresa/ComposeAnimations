package com.represa.draw

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.geometry.Size
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue


@ExperimentalPagerApi
@Composable
fun DotsIndicator() {
    var scope = rememberCoroutineScope()
    var dotSettings = IndicatorState.DotSettings(size = 5, radius = 12f, color = Color.Black)
    val pagerState = rememberPagerState(pageCount = 5)
    var state = remember { IndicatorState(scope, dotSettings, pagerState) }

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
                    .height(200.dp)
            ) {
            }
        }

        Indicators(state, Modifier.fillMaxWidth().padding(0.dp,10.dp,0.dp,0.dp))

        Row(verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxHeight()) {
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
    }
}

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

@Composable
fun Indicators(
    state: IndicatorState,
    modifier: Modifier
) {
    var dotSettings = state.dotSettings
    Canvas(
       modifier= modifier
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
                alpha = 0.3f
            )
        }
    }

    filledIndicator(state)
}

@Composable
fun filledIndicator(state: IndicatorState) {
    Canvas(
        Modifier.fillMaxWidth()
    ) {
        var distance = state.getSecondCircle().x - state.getFirstCircle().x
        //This is gonna be the first filled dot
        var firstDotAnimated = Offset(
            state.getFirstCircle().x + (distance * state.animation.value),
            state.firstDotPosition!!.y
        )
        drawCircle(
            color = Color.Black,
            radius = state.dotSettings.radius,
            center = firstDotAnimated,
            alpha = 1f
        )
        //This is gonna be the second filled one
        var secondDotAnimated = Offset(
            state.getFirstCircle().x + (distance * state.animationSecond.value),
            state.firstDotPosition!!.y
        )
        drawCircle(
            color = Color.Black,
            radius = state.dotSettings.radius,
            center = secondDotAnimated,
            alpha = 1f
        )
        //This gonna be the rectangle between filled dots
        var topleft = Offset(
            secondDotAnimated.x,
            state.firstDotPosition!!.y - state.dotSettings.radius
        )
        drawRect(
            color = Color.Black,
            topLeft = topleft,
            size = Size(
                (secondDotAnimated.x - firstDotAnimated.x).absoluteValue,
                state.dotSettings.radius * 2
            )
        )
    }
}


class IndicatorState @ExperimentalPagerApi constructor(
    private val scope: CoroutineScope,
    val dotSettings: DotSettings,
    private val pagerState: PagerState
) {
    var currentItem by mutableStateOf(0)
    var nextItem by mutableStateOf(1)
    var firstDotPosition: Offset? = null
    var animation = Animatable(initialValue = 0f)
    var animationSecond = Animatable(initialValue = 0f)
    private var isScrolling by mutableStateOf(false)

    @ExperimentalPagerApi
    fun scroll() {
        if (nextItem < dotSettings.size) {
            isScrolling = true
            scope.launch {
                nextItem = currentItem + 1
                launch {
                    //lazyListState.animateScrollToItem(nextItem)
                    pagerState.animateScrollToPage(nextItem)
                }
                launch {
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }.join()
                launch {
                    animationSecond.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
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

    @ExperimentalPagerApi
    fun scrollReverse() {
        if (currentItem > 0) {
            isScrolling = true
            scope.launch {
                nextItem = currentItem - 1
                launch {
                    //lazyListState.animateScrollToItem(nextItem)
                    pagerState.animateScrollToPage(nextItem)
                }
                launch {
                    animationSecond.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }.join()
                launch {
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }.join()
                animation.snapTo(0f)
                animationSecond.snapTo(0f)
                currentItem--
                isScrolling = false
            }
        }
    }

    fun setFirstIndicatorPosition(center: Offset) {
        firstDotPosition = Offset(
            center.x - (((dotSettings.size - 1) * dotSettings.distanceBetweenDots) / 2),
            center.y
        )
    }

    fun getFirstCircle() = Offset(
        firstDotPosition!!.x + dotSettings.distanceBetweenDots * currentItem,
        firstDotPosition!!.y
    )

    fun getSecondCircle() = Offset(
        firstDotPosition!!.x + dotSettings.distanceBetweenDots * nextItem,
        firstDotPosition!!.y
    )

    fun scrollEnabled() = nextItem < dotSettings.size && !isScrolling
    fun reverseScrollEnabled() = currentItem > 0 && !isScrolling

    class DotSettings(
        var size: Int,
        var radius: Float,
        var distanceBetweenDots: Float = radius * 7,
        var color: Color = Color.White
    )

}
