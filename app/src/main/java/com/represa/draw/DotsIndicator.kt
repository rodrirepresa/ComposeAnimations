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

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {
            Button(
                onClick = { state.scroll(state.firsIndicatorPosition+1) },
                enabled = state.scrollEnabled()
            ) {
                Text(text = "scroll")
            }
            Button(
                onClick = { state.scrollReverse(state.secondIndicatorPosition -1) },
                enabled = state.reverseScrollEnabled()
            ) {
                Text(text = "scroll reverse")
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun test(state: IndicatorState, pagerState: PagerState) {
    //var currentValue = state.currentValue
    //state.scrollReverse(currentValue)
    if(pagerState.isScrollInProgress && state.targetPosition != pagerState.targetPage){
        state.startScrolling(pagerState.targetPage!!)
    }else if(!pagerState.isScrollInProgress){
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

    //filledIndicator(state)

    firstFilledDot(state)
    secondFilledCircle(state)
}

@ExperimentalPagerApi
@Composable
fun firstFilledDot(state: IndicatorState) {
    if(state.stateFirstDot == IndicatorState.DotState.SCROLLING) {
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
    }else{
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
    if(state.stateSecondDot == IndicatorState.DotState.SCROLLING) {

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
                color = Color.Green,
                radius = state.dotSettings.radius,
                center = secondDotAnimated,
                alpha = 0.8f
            )
        }
    }else{
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
                color = Color.Green,
                radius = state.dotSettings.radius,
                center = secondDotAnimated,
                alpha = 0.8f
            )
        }
    }
}

@Composable
fun filledIndicator(state: IndicatorState) {
    Canvas(
        Modifier.fillMaxWidth()
    ) {
        var targetValue = state.firsIndicatorPosition
        var currentValue = state.secondIndicatorPosition
        var targetPosition = state.getTargetValue(targetValue)
        var currentPosition = state.getTargetValue(currentValue)
        var distanceFirstCircle = (targetPosition.x - currentPosition.x)

        var distance = state.getSecondCircle().x - state.getFirstCircle().x
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
        //This is gonna be the second filled one
        var secondDotAnimated = Offset(
            currentPosition.x + (distanceFirstCircle * state.animationSecond.value),
            state.firstDotPosition!!.y
        )
        drawCircle(
            color = Color.Green,
            radius = state.dotSettings.radius,
            center = secondDotAnimated,
            alpha = 0.8f
        )
        //This gonna be the rectangle between filled dots
        var topleft = Offset(
            secondDotAnimated.x,
            state.firstDotPosition!!.y - state.dotSettings.radius
        )
        /*drawRect(
            color = Color.Black,
            alpha = 0.8f,
            topLeft = topleft,
            size = Size(
                (secondDotAnimated.x - firstDotAnimated.x).absoluteValue,
                state.dotSettings.radius * 2
            )
        )*/
    }
}




class IndicatorState @ExperimentalPagerApi constructor(
    val scope: CoroutineScope,
    val dotSettings: DotSettings
) {

    enum class DotState{
        IDLE,SCROLLING_LEFT,SCROLLING_RIGHT,SCROLLING
    }

    var currentItem by mutableStateOf(0)
    var nextItem by mutableStateOf(1)
    var firstDotPosition: Offset? = null
    var animation = Animatable(initialValue = 0f)
    var animationSecond = Animatable(initialValue = 0f)
    private var isScrolling by mutableStateOf(false)

    var firsIndicatorPosition by mutableStateOf(0)
    var targetFirstIndicatorPostion by mutableStateOf(0)
    var secondIndicatorPosition by mutableStateOf(0)
    var targetSecondIndicatorPostion by mutableStateOf(0)
    var test = DotState.IDLE


    var currentPosition = 0
    var targetPosition = 0
    var stateFirstDot by mutableStateOf(DotState.IDLE)
    var stateSecondDot by mutableStateOf(DotState.IDLE)

    fun startScrolling(targetValue: Int){
        if(targetValue < dotSettings.size && currentPosition < dotSettings.size){
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
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }
            }
        //}
    }

    fun finishScrolling(){
        stateSecondDot = DotState.IDLE
        if(targetPosition != currentPosition){
            scope.launch {
                stateSecondDot = DotState.SCROLLING
                animationSecond.snapTo(0f)
                animationSecond.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                )
                currentPosition = targetPosition
                stateSecondDot = DotState.IDLE
            }
        }else{
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


    @ExperimentalPagerApi
    fun scroll(target: Int) {
        if (secondIndicatorPosition < dotSettings.size) {
            isScrolling = true
            scope.launch {
                //nextItem = currentItem + 1
                //firsIndicatorPosition = target
                launch {
                    //lazyListState.animateScrollToItem(nextItem)
                    //pagerState.animateScrollToPage(nextItem)
                }
                launch {
                    animation.snapTo(0f)
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }.join()
                //animation.snapTo(0f)
                //animationSecond.snapTo(0f)
                //currentItem++
                //nextItem++
            }
        }
    }

    @ExperimentalPagerApi
    fun scrollSecond() {
        if (secondIndicatorPosition < dotSettings.size) {
            scope.launch {
                //nextItem = currentItem + 1
                launch {
                    //lazyListState.animateScrollToItem(nextItem)
                    //pagerState.animateScrollToPage(nextItem)
                }
                launch {
                    animationSecond.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }.join()
                //animation.snapTo(0f)
                animationSecond.snapTo(0f)
                animation.snapTo(0f)
                secondIndicatorPosition++
                //currentItem++
                //nextItem++
                isScrolling = false
            }
        }
    }

    @ExperimentalPagerApi
    fun scrollReverse(target: Int) {
        if (secondIndicatorPosition > 0) {
            isScrolling = true
            scope.launch {
                //nextItem = currentItem - 1
                firsIndicatorPosition = target
                launch {
                    //lazyListState.animateScrollToItem(nextItem)
                    //pagerState.animateScrollToPage(nextItem)
                }
                launch {
                    animationSecond.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }.join()
                //animation.snapTo(0f)
                //animationSecond.snapTo(0f)
                //currentItem--
            }
        }
    }

    fun scrollReverseFirst() {
        if (secondIndicatorPosition > 0) {
            scope.launch {
                //nextItem = currentItem - 1
                launch {
                    //lazyListState.animateScrollToItem(nextItem)
                    //pagerState.animateScrollToPage(nextItem)
                }
                launch {
                    animation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
                }.join()
                animationSecond.snapTo(0f)
                animation.snapTo(0f)
                secondIndicatorPosition--
                //currentItem++
                //nextItem++
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

    fun getTargetValue(target: Int) = Offset(
        firstDotPosition!!.x + dotSettings.distanceBetweenDots * target,
        firstDotPosition!!.y
    )

    fun scrollEnabled() = secondIndicatorPosition+1 < dotSettings.size && !isScrolling
    fun reverseScrollEnabled() = secondIndicatorPosition > 0 && !isScrolling

    class DotSettings(
        var size: Int,
        var radius: Float,
        var distanceBetweenDots: Float = radius * 5,
        var color: Color = Color.White
    )

}
