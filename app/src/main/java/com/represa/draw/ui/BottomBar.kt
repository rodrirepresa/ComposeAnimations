package com.represa.draw.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.represa.draw.extensions.safeLet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
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

    var subCategoryItems = listOf(
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

    val listState = rememberLazyListState()
    val gridListState = rememberLazyListState()

    var navigationBarVisibility = remember { mutableStateOf(true) }
    val density = LocalDensity.current

    var contentPadding = with(density) { 10.dp.toPx() }
    var scope = rememberCoroutineScope()
    var bottomBarState = remember {
        BottomBarState(scope)
    }

    //Debug info


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFCFF)),
        contentAlignment = Alignment.BottomStart
    ) {

        Background(gridListState, navigationBarVisibility)

        AnimatedVisibility(
            visible = navigationBarVisibility.value,
            enter = slideInVertically(
                // Slide in from 40 dp from the top.
                initialOffsetY = { with(density) { 40.dp.roundToPx() } },
                animationSpec = tween(durationMillis = 100, easing = LinearEasing)
            ) + fadeIn(
                // Fade in with the initial alpha of 0.3f.
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically(
                targetOffsetY = { with(density) { 40.dp.roundToPx() } },
                animationSpec = tween(durationMillis = 100, easing = LinearEasing)

            ) + fadeOut(
                animationSpec = tween(durationMillis = 100, easing = LinearEasing)
            )
        ) {

            AnimatedNavigationBar(
                listState = listState,
                bottomBarState = bottomBarState,
                items = items,
                subCategoryItems = subCategoryItems,
                density = density,
                contentPadding = contentPadding
            )

        }
    }

    Column() {
        Text(
            text = """" 
                ${listState.layoutInfo.viewportStartOffset}
                ${listState.layoutInfo.viewportEndOffset}
            """.trimMargin()
        )

        listState.layoutInfo.visibleItemsInfo.forEach {
            Text(
                text = """
            ${it.index} + "/" + "offsetStart: ${it.offset} + "/" + "size: ${it.size} + "///" + "offsetEnd: ${it.size + it.offset}
        """.trimIndent()
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedNavigationBar(
    listState: LazyListState,
    bottomBarState: BottomBarState,
    items: List<String>,
    subCategoryItems: List<String>,
    density: Density,
    contentPadding: Float
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Bottom
    ) {

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp, 0.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                elevation = 10.dp,
                shape = RoundedCornerShape(30.dp),

                ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CategoriesRow(
                        state = listState,
                        bottomBarState = bottomBarState,
                        items = items,
                        contentPadding = contentPadding,
                        density = density
                    )
                    SubCategoryRow(
                        state = listState,
                        bottomBarState = bottomBarState,
                        items = subCategoryItems,
                        contentPadding = contentPadding,
                        density = density
                    )
                }
            }
        }

        FakeNavigationBar()

    }
}

@ExperimentalAnimationApi
@Composable
fun CategoriesRow(
    state: LazyListState,
    bottomBarState: BottomBarState,
    items: List<String>,
    contentPadding: Float,
    density: Density
) {

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

        DrawIndicator(state, bottomBarState, contentPadding, false, density)
        Categories(state, bottomBarState, items, false)
    }
}

@ExperimentalAnimationApi
@Composable
fun SubCategoryRow(
    state: LazyListState,
    bottomBarState: BottomBarState,
    items: List<String>,
    contentPadding: Float,
    density: Density
) {

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


        Box() {
            DrawIndicator(state, bottomBarState, contentPadding, true, density)
            Categories(state, bottomBarState, items, true)
        }

        Box(modifier = Modifier.wrapContentSize()) {
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(40.dp)
                    .padding(10.dp, 10.dp, 0.dp, 10.dp),
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
    subCategory: Boolean,
    density: Density
) {
    //When we show the back arrow, we have to add this size to calculate properly the beggining Offset of our Box()
    var backArrowOffset = if (subCategory) {
        with(density) { 40.dp.toPx() }
    } else {
        0f
    }

    var height = with(density) { 30.dp.toPx() }

    //We have to rest the start margin
    // subCategory -> 20.dp - 15.dp = 5.dp
    //else 10.dp
    var sizeOffset = if (subCategory) {
        with(density) { 5.dp.toPx() }
    } else {
        contentPadding
    }

    var center = if (subCategory) {
        Offset(with(density) { 40.dp.toPx() }, 0f)
    } else {
        Offset(with(density) { 0.dp.toPx() }, 0f)
    }

    Canvas(modifier = Modifier) {
        translate(center.x, center.y) {
            when (bottomBarState.animationState) {
                AnimationState.SCROLLING -> {
                    with(bottomBarState) {
                        state.getDistance(previousIndex, currentIndex)?.let { distance ->
                            drawRoundRect(
                                color = Color.Blue,
                                topLeft = Offset(
                                    state.getTopLeftAxisX(
                                        previousIndex,
                                        sizeOffset,
                                        0f
                                    ) + (distance * animation.value),
                                    contentPadding
                                ),
                                size = state.getSize(currentIndex, sizeOffset, height),
                                cornerRadius = CornerRadius(40f)
                            )
                        } ?: run {

                            var to = state.getItem(currentIndex)

                            var topLeft = when {
                                currentIndex > previousIndex -> {
                                    Offset(
                                        (to!!
                                            .offset + sizeOffset ) * animation.value,
                                        contentPadding
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
                                size = state.getSize(currentIndex, contentPadding, height),
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
                                state.getTopLeftAxisX(
                                    currentIndex,
                                    contentPadding = sizeOffset
                                ),
                                contentPadding
                            ),
                            size = state.getSize(currentIndex, contentPadding, height),
                            cornerRadius = CornerRadius(40f)
                        )
                    }
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
    var padding = if (subCategory) {
        PaddingValues(25.dp, 0.dp, 0.dp, 0.dp)
    } else {
        PaddingValues(0.dp, 0.dp)
    }
    var paddingValues = if (subCategory) {
        PaddingValues(20.dp, 0.dp)
    } else {
        PaddingValues(10.dp, 0.dp)
    }

    LazyRow(
        modifier = Modifier.padding(padding),
        contentPadding = paddingValues,
        state = state
    ) {
        itemsIndexed(items) { index, item ->
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

@ExperimentalFoundationApi
@Composable
fun Background(gridListState: LazyListState, navigationBarVisibility: MutableState<Boolean>) {

    var offset = remember { mutableStateOf(0) }
    var first = remember { mutableStateOf(0) }
    gridListState.setScrollBehaviour(first, offset, navigationBarVisibility)

    LazyVerticalGrid(
        state = gridListState,
        cells = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(13.dp, 10.dp)
    ) {
        items(50) { item ->
            var padding = if (item % 2 == 0) {
                PaddingValues(0.dp, 0.dp, 3.dp, 6.dp)
            } else {
                PaddingValues(3.dp, 0.dp, 3.dp, 6.dp)
            }
            Box(
                modifier = Modifier
                    .height(190.dp)
                    .padding(padding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFeeeeee))
                ) {

                }
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
    contentPadding: Float = 0f,
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

private fun LazyListState.getSize(
    index: Int,
    contentPadding: Float = 0f,
    height: Float = 0f
): Size {
    var item = getItem(index)
    item?.let {
        return Size(item.size.toFloat() - contentPadding, height)
    } ?: run {
        return Size.Zero
    }
}

private fun LazyListState.setScrollBehaviour(
    first: MutableState<Int>,
    offset: MutableState<Int>,
    navigationBarVisibility: MutableState<Boolean>
) {
    if (isScrollInProgress) {
        layoutInfo.visibleItemsInfo.firstOrNull()?.let { firstItem ->
            when {
                firstItem.index > first.value -> {
                    navigationBarVisibility.value = false
                    offset.value = 0
                }
                firstItem.index < first.value -> {
                    navigationBarVisibility.value = true
                    offset.value = firstItem.offset
                }
                else -> {
                    when {
                        firstItem.offset < offset.value -> {
                            navigationBarVisibility.value = false
                        }
                        firstItem.offset > offset.value -> {
                            navigationBarVisibility.value = true
                        }
                    }
                    offset.value = firstItem.offset
                }
            }
            first.value = firstItem.index
        }
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
                    animationSpec = tween(durationMillis = 150, easing = LinearEasing)
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
                    animationSpec = tween(durationMillis = 150, easing = LinearEasing)
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

@Composable
fun FakeNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(0.dp, 8.dp, 0.dp, 0.dp)
            .background(Color.White)
    ) {
        for (i in 0..4) {
            var (icon, title) =
                when (i) {
                    0 -> Pair(Icons.Outlined.Home, "Home")
                    1 -> Pair(Icons.Outlined.Search, "Search")
                    2 -> Pair(Icons.Outlined.FavoriteBorder, "Favourites")
                    3 -> Pair(Icons.Outlined.Face, "My Account")
                    4 -> Pair(Icons.Outlined.Settings, "Settings")
                    else -> Pair(Icons.Outlined.Settings, "Settings")
                }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = if (i == 0) {
                        Color.DarkGray
                    } else {
                        Color.LightGray
                    }
                )
                Text(
                    text = title,
                    color = if (i == 0) {
                        Color.DarkGray
                    } else {
                        Color.LightGray
                    }
                )
            }
        }

    }
}