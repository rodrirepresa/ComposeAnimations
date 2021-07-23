package com.represa.draw.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.represa.draw.extensions.safeLet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomBar() {

    var items = listOf(
        "HOME",
        "NEW IN",
        "CLOTHING",
        "DENIM",
        "SHOES",
        "ACCESORIES",
        "SALE",
        "JUNIOR",
        "PLUS SIZE"
    )

    //Experiment with Density and Offset

    var e = with(LocalDensity.current) { 100.dp.toPx() }

    Box(modifier = Modifier
        .fillMaxSize()
        .drawBehind { drawCircle(Color.Blue, 3f, Offset(e, e)) }) {
        Box(modifier = Modifier.padding(100.dp, 100.dp)) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Red)
            )
        }

    }

    var state = rememberLazyListState()

    //Debug info

    Column() {
        Text(
            text = """" 
                ${state.layoutInfo.viewportStartOffset}
                ${state.layoutInfo.viewportEndOffset}
            """.trimMargin()
        )

        state.layoutInfo.visibleItemsInfo.forEach {
            Text(
                text = """
            ${it.index} + "/" + "offsetStart: ${it.offset} + "/" + "size: ${it.size} + "///" + "offsetEnd: ${it.size + it.offset}
        """.trimIndent()
            )
        }
    }

    var contentPadding = with(LocalDensity.current) { 10.dp.toPx() }
    var scope = rememberCoroutineScope()
    var bottomBarState = remember {
        BottomBarState(scope)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp)

        ) {
            DrawIndicator(state, bottomBarState, contentPadding)
            Categories(state, bottomBarState, items)
        }
    }

}

@Composable
fun DrawIndicator(state: LazyListState, bottomBarState: BottomBarState, contentPadding: Float) {
    Canvas(modifier = Modifier) {
        when (bottomBarState.animationState) {
            AnimationState.SCROLLING -> {
                with(bottomBarState) {
                    var from = state.getItem(previousIndex)
                    var to = state.getItem(currentIndex)
                    safeLet(to, from) { to, from ->
                        var distance = to.offset - from.offset
                        drawRect(
                            color = Color.Red,
                            topLeft = Offset(
                                from.offset.toFloat() + contentPadding + (distance * animation.value),
                                contentPadding
                            ),
                            size = Size(to.size.toFloat() - contentPadding, contentPadding * 3)
                        )
                    } ?: kotlin.run {
                        var distance = when {
                            currentIndex > previousIndex -> {
                                Offset(
                                    to!!
                                        .offset * animation.value, contentPadding
                                )
                            }
                            currentIndex < previousIndex -> {
                                Offset(
                                    state.layoutInfo.viewportEndOffset - (state.layoutInfo.viewportEndOffset - to!!.offset - contentPadding) * (animation.value),
                                    contentPadding
                                )
                            }
                            else -> {
                                Offset(to!!.offset.toFloat(), contentPadding)
                            }
                        }
                        drawRect(
                            color = Color.Red,
                            topLeft = distance,
                            size = Size(to.size.toFloat() - contentPadding, contentPadding * 3)
                        )
                    }
                }
            }
            AnimationState.IDLE -> {
                state.getItem(bottomBarState.currentIndex)?.let {
                    drawRect(
                        color = Color.Red,
                        topLeft = Offset(it.offset.toFloat() + contentPadding, contentPadding),
                        size = Size(it.size.toFloat() - contentPadding, contentPadding * 3)
                    )
                }
            }
        }
    }
}

@Composable
fun Categories(state: LazyListState, bottomBarState: BottomBarState, items: List<String>) {

    var scope = rememberCoroutineScope()

    LazyRow(
        modifier = Modifier
            .padding(0.dp, 10.dp),
        contentPadding = PaddingValues(10.dp, 0.dp),
        state = state
    ) {
        itemsIndexed(items) { index, item ->
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .padding(0.dp, 0.dp, 10.dp, 0.dp)
                    //.background(Color.Blue)
                    .clickable {
                        //bottomBarState.currentIndex = index
                        state
                            .getItem(index)
                            ?.let { item ->
                                var positionFromMiddle = when {
                                    state.layoutInfo.viewportEndOffset / 2 < item.offset + item.size -> {
                                        Position.RIGHT
                                    }
                                    state.layoutInfo.viewportEndOffset / 2 > item.offset + item.size -> {
                                        Position.LEFT
                                    }
                                    else -> {
                                        Position.IDLE
                                    }
                                }
                                scope.launch {
                                    state.animateScrollBy(
                                        state.toScroll(
                                            index,
                                            positionFromMiddle,
                                            bottomBarState
                                        )
                                    )
                                }
                            }

                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item, modifier = Modifier
                        .padding(20.dp, 0.dp)
                )
            }
        }

    }

}


private fun LazyListState.toScroll(
    index: Int,
    positionFromMiddle: Position,
    bottomBarState: BottomBarState
): Float {
    getItem(index)?.let { item ->
        return when (positionFromMiddle) {
            Position.RIGHT -> {
                bottomBarState.scroll(index)
                item.offset - (layoutInfo.viewportEndOffset / 2f) + item.size / 2
            }
            Position.LEFT -> {
                bottomBarState.scroll(index)
                item.size / 2 + item.offset - (layoutInfo.viewportEndOffset / 2f)
            }
            Position.IDLE -> 0f
        }
    } ?: kotlin.run {
        return@toScroll 0f
    }
}

private fun LazyListState.getItem(index: Int): LazyListItemInfo? {
    return layoutInfo.visibleItemsInfo.firstOrNull() {
        it.index == index
    }
}

enum class Position {
    RIGHT,
    LEFT,
    IDLE
}

enum class AnimationState {
    SCROLLING,
    IDLE
}

class BottomBarState(var scope: CoroutineScope) {

    var animation = Animatable(initialValue = 0f)
    var previousIndex = 0
    var currentIndex = 0
    var animationState by mutableStateOf(AnimationState.IDLE)


    fun scroll(index: Int) {
        previousIndex = currentIndex
        currentIndex = index
        scope.launch {
            animationState = AnimationState.SCROLLING
            animation.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 100, easing = LinearEasing)
            )
            animationState = AnimationState.IDLE
            animation.snapTo(0f)
        }
    }

}