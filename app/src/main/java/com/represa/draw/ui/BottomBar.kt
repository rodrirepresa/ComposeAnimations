package com.represa.draw.ui

import androidx.compose.animation.*
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.represa.draw.extensions.safeLet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
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

    var items2 = listOf(
        "ALL CLOTHING",
        "SEASON HIGHLIGHT",
        "T-SHIRT",
        "DENIM",
        "VIEW ALL",
        "JACKETS",
        "JEANS",
        "TROUSERS",
        "SHORTS"
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
            shape = RoundedCornerShape(30.dp)

        ) {
            CategoriesRow(
                state = state,
                bottomBarState = bottomBarState,
                items = items,
                contentPadding = contentPadding
            )
            SubCategoryRow(
                state = state,
                bottomBarState = bottomBarState,
                items = items2,
                contentPadding = contentPadding
            )
        }
    }

}

@ExperimentalAnimationApi
@Composable
fun CategoriesRow(
    state: LazyListState,
    bottomBarState: BottomBarState,
    items: List<String>,
    contentPadding: Float
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = bottomBarState.categoriesVisibility,
        enter = slideInVertically(
            // Slide in from 40 dp from the top.
            initialOffsetY = { with(density) { -40.dp.roundToPx() } },
            animationSpec = tween(durationMillis = 300, easing = LinearEasing)
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 300, easing = LinearEasing)

        ) + fadeOut(
            animationSpec = tween(durationMillis = 300, easing = LinearEasing)
        )
    ) {

        DrawIndicator(state, bottomBarState, contentPadding, false)
        Categories(state, bottomBarState, items, false)
    }
}

@ExperimentalAnimationApi
@Composable
fun SubCategoryRow(
    state: LazyListState,
    bottomBarState: BottomBarState,
    items: List<String>,
    contentPadding: Float
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = bottomBarState.subCategoriesVisibility,
        enter = slideInVertically(
            // Slide in from 40 dp from the top.
            initialOffsetY = { with(density) { 40.dp.roundToPx() } },
            animationSpec = tween(durationMillis = 300, easing = LinearEasing)
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically(
            targetOffsetY = { with(density) { 40.dp.roundToPx() } },
            animationSpec = tween(durationMillis = 300, easing = LinearEasing)

        ) + fadeOut(
            animationSpec = tween(durationMillis = 300, easing = LinearEasing)
        )
    ) {

        DrawIndicator(state, bottomBarState, contentPadding, true)
        Categories(state, bottomBarState, items, true)
    }

}

/**
 * @contentPadding -> Represent the margin of our Box(), needed to show properly the blue indicator
 */
@Composable
fun DrawIndicator(
    state: LazyListState,
    bottomBarState: BottomBarState,
    contentPadding: Float,
    subCategory: Boolean
) {
    //When we show the back arrow, we have to add this size to calculate properly the beggining Offset of our Box()
    var backArrowOffset = if (subCategory && bottomBarState.currentIndex == 0) {
        with(LocalDensity.current) { 40.dp.toPx() }
    } else {
        0f
    }

    //As our Box() has a 10.dp padding, we need to rest this padding to show the indicator with the size of the text
    var sizeOffset = backArrowOffset + contentPadding

    Canvas(modifier = Modifier) {
        when (bottomBarState.animationState) {
            AnimationState.SCROLLING -> {
                with(bottomBarState) {
                    state.getDistance(previousIndex, currentIndex)?.let { distance ->
                        drawRoundRect(
                            color = Color.Blue,
                            topLeft = Offset(
                                state.getTopLeftAxisX(
                                    previousIndex,
                                    contentPadding,
                                    backArrowOffset
                                ) + (distance * animation.value),
                                contentPadding
                            ),
                            size = state.getSize(currentIndex, sizeOffset, contentPadding * 3),
                            cornerRadius = CornerRadius(40f)
                        )
                    } ?: run {

                        var to = state.getItem(currentIndex)

                        var topLeft = when {
                            currentIndex > previousIndex -> {
                                Offset(
                                    (to!!
                                        .offset + contentPadding) * animation.value, contentPadding
                                )
                            }
                            currentIndex < previousIndex -> {
                                Offset(
                                    state.layoutInfo.viewportEndOffset - (state.layoutInfo.viewportEndOffset - to!!.offset - contentPadding) * (animation.value) + backArrowOffset,
                                    contentPadding
                                )
                            }
                            else -> {
                                Offset(to!!.offset.toFloat(), contentPadding)
                            }
                        }
                        drawRoundRect(
                            color = Color.Blue,
                            topLeft = topLeft,
                            size = state.getSize(currentIndex, contentPadding, contentPadding * 3),
                            cornerRadius = CornerRadius(40f)
                        )
                    }
                }
            }
            AnimationState.IDLE -> {
                with(bottomBarState) {
                    drawRoundRect(
                        color = Color.Blue,
                        topLeft = Offset(
                            state.getTopLeftAxisX(currentIndex, contentPadding, backArrowOffset),
                            contentPadding
                        ),
                        size = state.getSize(currentIndex, sizeOffset, contentPadding * 3),
                        cornerRadius = CornerRadius(40f)
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Categories(
    state: LazyListState,
    bottomBarState: BottomBarState,
    items: List<String>,
    subCategory: Boolean
) {

    var scope = rememberCoroutineScope()

    LazyRow(
        modifier = Modifier,
        contentPadding = PaddingValues(10.dp, 0.dp),
        state = state
    ) {
        itemsIndexed(items) { index, item ->


            if (subCategory && index == 0) {
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(40.dp)
                        .padding(0.dp, 10.dp, 10.dp, 10.dp),
                    elevation = 5.dp,
                    shape = RoundedCornerShape(30.dp),
                    backgroundColor = Color.LightGray,
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier
                            .scale(0.6f)
                            .clickable {
                                bottomBarState.reset(state)
                            })
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .padding(0.dp, 0.dp, 10.dp, 0.dp)
                    //.background(Color.Yellow)
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
                                            bottomBarState,
                                            state
                                        )
                                    )
                                }
                            }

                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item, modifier = Modifier
                        .padding(20.dp, 0.dp),
                    fontWeight = FontWeight.Bold,
                    color = if (bottomBarState.currentIndex == index) Color.White else Color.Black
                )
            }
        }

    }

}

private fun LazyListState.toScroll(
    index: Int,
    positionFromMiddle: Position,
    bottomBarState: BottomBarState,
    state: LazyListState
): Float {
    getItem(index)?.let { item ->
        return when (positionFromMiddle) {
            Position.RIGHT -> {
                bottomBarState.scroll(index, state)
                item.offset - (layoutInfo.viewportEndOffset / 2f) + item.size / 2
            }
            Position.LEFT -> {
                bottomBarState.scroll(index, state)
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

private fun LazyListState.getTopLeftAxisX(
    index: Int,
    contentPadding: Float,
    backArrowOffset: Float = 0f
): Float {
    var item = getItem(index)
    item?.let {
        return item.offset.toFloat() + contentPadding + backArrowOffset
    } ?: run {
        return 0f
    }
}

private fun LazyListState.getDistance(from: Int, to: Int): Int? {
    var from = getItem(from)
    var to = getItem(to)
    safeLet(from, to) { from, to ->
        return to.offset - from.offset
    } ?: run {
        return null
    }
}

private fun LazyListState.getSize(index: Int, contentPadding: Float, height: Float): Size {
    var item = getItem(index)
    item?.let {
        return Size(item.size.toFloat() - contentPadding, height)
    } ?: run {
        return Size.Zero
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
    var currentIndex by mutableStateOf(0)
    var animationState by mutableStateOf(AnimationState.IDLE)

    var categoriesVisibility by mutableStateOf(true)
    var subCategoriesVisibility by mutableStateOf(false)


    fun scroll(index: Int, state: LazyListState) {
        previousIndex = currentIndex
        currentIndex = index
        if (categoriesVisibility && !subCategoriesVisibility) {
            scope.launch {
                animationState = AnimationState.SCROLLING
                animation.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                )
                categoriesVisibility = !categoriesVisibility
                delay(350)
                subCategoriesVisibility = !subCategoriesVisibility
                animationState = AnimationState.IDLE
                animation.snapTo(0f)
                previousIndex = 0
                currentIndex = 0
                state.scrollToItem(0)
            }
        } else if (!categoriesVisibility && subCategoriesVisibility) {
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

    fun reset(state: LazyListState) {
        scope.launch {
            subCategoriesVisibility = !subCategoriesVisibility
            delay(300)
            categoriesVisibility = !categoriesVisibility
            animationState = AnimationState.IDLE
            animation.snapTo(0f)
            previousIndex = 0
            currentIndex = 0
            state.scrollToItem(0)
        }
    }

}