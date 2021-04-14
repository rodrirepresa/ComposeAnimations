package com.represa.draw

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.*
import kotlin.math.*

@Composable
fun PhysicoChest() {
    var scope = rememberCoroutineScope()
    var state = remember { ChestState(scope) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Chest(state)
    }
}

private val colorLight = Color(0xFFB2EBF2)
private val colorDark = Color(0xFF263238)
private val square = Size(100f, 100f)

class ChestState(private val scope: CoroutineScope) {
    var topLeftProgressHorizontal = Animatable(initialValue = 0f)
    var ovalSizeProgressHorizontal = Animatable(initialValue = -90f)
    var topLeftProgressVertical = Animatable(initialValue = 0f)
    var ovalSizeProgressVertical = Animatable(initialValue = -90f)
    var color by mutableStateOf(colorLight)
    var drawHorizontal by mutableStateOf(true)
    var drawVertical by mutableStateOf(false)

    var horizontalfinish by mutableStateOf(false)
    var verticalfinish by mutableStateOf(true)

    private val delay = 3000
    private val delay_stop = 1000L

    init {
        scope.launch {
            delay(delay_stop)
            while (isActive) {
                startHorizontal().join()
                horizontalfinish = true
                delay(delay_stop)
                drawHorizontal = false
                startVertical().join()
                verticalfinish = true
                delay(delay_stop)
                drawVertical = false
            }
        }
    }

    private suspend fun startHorizontal() = scope.launch {
        horizontalfinish = false
        drawHorizontal = true
        color = colorLight
        launch {
            ovalSizeProgressHorizontal.animateTo(
                targetValue = 90f,
                animationSpec = tween(durationMillis = delay, easing = LinearEasing)
            )
            ovalSizeProgressHorizontal.snapTo(-90f)
        }
        launch {
            topLeftProgressHorizontal.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = delay, easing = LinearEasing)
            )
            topLeftProgressHorizontal.snapTo(0f)
        }
    }

    private fun startVertical() = scope.launch {
        drawVertical = true
        verticalfinish = false
        color = colorDark
        launch {
            ovalSizeProgressVertical.animateTo(
                targetValue = 90f,
                animationSpec = tween(durationMillis = delay, easing = LinearEasing)
            )
            ovalSizeProgressVertical.snapTo(-90f)
        }
        launch {
            topLeftProgressVertical.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = delay, easing = LinearEasing)
            )
            topLeftProgressVertical.snapTo(0f)
        }
    }
}


@Composable
private fun Chest(state: ChestState) {

    Box(
        Modifier
            .fillMaxSize()
            .background(state.color)
            .drawBehind {

                var squareWidthAmount = floor(size.width / square.width)
                var restWidth = size.width.rem(square.width)

                var squareHeightAmount = floor(size.height / square.height)
                var restHeight = size.height.rem(square.height)

                translate(restWidth / 2, restHeight / 2) {

                    var position: Offset

                    for (r in 0 until squareHeightAmount.toInt()) {
                        for (c in 0 until squareWidthAmount.toInt()) {
                            position = Offset.Zero + Offset(square.width * c, square.height * r)
                            when {
                                r % 2 == 1 && c % 2 == 1 && state.drawHorizontal -> {
                                    horizontalLeft(state, position)
                                }
                                r % 2 == 1 && c % 2 == 0 && state.drawVertical -> {
                                    verticalDown(state, position)
                                }
                                r % 2 == 0 && c % 2 == 1 && state.drawVertical -> {
                                    verticalUp(state, position)
                                }
                                r % 2 == 0 && c % 2 == 0 && state.drawHorizontal -> {
                                    horizontalRight(state, position)
                                }
                            }
                        }
                    }
                }
            }
    )
}

private fun DrawScope.horizontalRight(state: ChestState, leftCorner: Offset) {
    var ovalSizeProgress = state.ovalSizeProgressHorizontal.value
    var topLeftProgress = state.topLeftProgressHorizontal.value
    var theta = PI.toFloat() * ovalSizeProgress / 180f

    var c2 = Offset(leftCorner.x + square.width * (1 - topLeftProgress), leftCorner.y)
    var s2 = Size(square.width * cos(theta), square.height)
    var l2 = Offset(c2.x - s2.width / 2, leftCorner.y)

    var c1 = Offset((leftCorner.x + square.width * topLeftProgress), leftCorner.y)
    var s1 = Size(square.width * cos(theta), square.height)
    var l1 = Offset(c1.x - s1.width / 2, leftCorner.y)

    //Oval from right to left
    drawOval(
        color = colorDark,
        topLeft = l2,
        size = s2,
        style = Fill
    )

    var centre = when {
        c1.x <= c2.x -> c1
        else -> c2
    }

    drawRect(
        color = colorDark,
        topLeft = centre,
        size = Size((c1.x - c2.x).absoluteValue, square.height)
    )

    //left to right
    if (!state.horizontalfinish) {
        drawOval(
            color = colorLight,
            topLeft = l1,
            size = s1,
            style = Stroke(4f)
        )
    }

    drawOval(
        color = colorDark,
        topLeft = l1,
        size = s1,
        style = Fill
    )
}

private fun DrawScope.horizontalLeft(state: ChestState, leftCorner: Offset) {
    var animatedProgress = state.ovalSizeProgressHorizontal.value
    var sizePRogress = state.topLeftProgressHorizontal.value
    var theta = PI.toFloat() * animatedProgress / 180f

    var c2 = Offset(leftCorner.x + square.width * (1 - sizePRogress), leftCorner.y)
    var s2 = Size(square.width * cos(theta), square.height)
    var l2 = Offset(c2.x - s2.width / 2, leftCorner.y)

    var c1 = Offset((leftCorner.x + square.width * sizePRogress), leftCorner.y)
    var s1 = Size(square.width * cos(theta), square.height)
    var l1 = Offset(c1.x - s1.width / 2, leftCorner.y)

    //left to right
    drawOval(
        color = colorDark,
        topLeft = l1,
        size = s1,
        style = Fill
    )

    var centre = when {
        c1.x <= c2.x -> c1
        else -> c2
    }

    drawRect(
        color = colorDark,
        topLeft = centre,
        size = Size((c1.x - c2.x).absoluteValue, square.height)
    )

    //right to left
    if (!state.horizontalfinish) {
        drawOval(
            color = colorLight,
            topLeft = l2,
            size = s2,
            style = Stroke(4f)
        )
    }

    drawOval(
        color = colorDark,
        topLeft = l2,
        size = s2,
        style = Fill
    )
}

private fun DrawScope.verticalDown(state: ChestState, leftCorner: Offset) {
    var animatedProgress = state.ovalSizeProgressVertical.value
    var sizePRogress = state.topLeftProgressVertical.value
    var theta = PI.toFloat() * animatedProgress / 180f

    var c2 = Offset(leftCorner.x, (leftCorner.y + square.height * (1 - sizePRogress)))
    var s2 = Size(square.width, square.height * cos(theta))
    var l2 = Offset(leftCorner.x, c2.y - s2.height / 2)

    var c1 = Offset(leftCorner.x, leftCorner.y + square.height * sizePRogress)
    var s1 = Size(square.width, square.height * cos(theta))
    var l1 = Offset(leftCorner.x, c1.y - s1.height / 2)

    //Oval from bottom to top
    drawOval(
        color = colorLight,
        topLeft = l2,
        size = s2,
        style = Fill
    )

    var centre = when {
        c1.y <= c2.y -> c1
        else -> c2
    }

    drawRect(
        color = colorLight,
        topLeft = centre,
        size = Size(square.width, (c1.y - c2.y).absoluteValue)
    )

    //Oval from top to bottom
    if (!state.verticalfinish) {
        drawOval(
            color = colorDark,
            topLeft = l1,
            size = s1,
            style = Stroke(4f)
        )
    }

    drawOval(
        color = colorLight,
        topLeft = l1,
        size = Size(s1.width - 2, s1.height - 2),
        style = Fill
    )
}

private fun DrawScope.verticalUp(state: ChestState, leftCorner: Offset) {
    var animatedProgress = state.ovalSizeProgressVertical.value
    var sizePRogress = state.topLeftProgressVertical.value
    var theta = PI.toFloat() * animatedProgress / 180f

    var c2 = Offset(leftCorner.x, leftCorner.y + square.height * (1 - sizePRogress))
    var s2 = Size(square.width, square.height * cos(theta))
    var l2 = Offset(leftCorner.x, c2.y - s2.height / 2)

    var c1 = Offset(leftCorner.x, leftCorner.y + square.height * sizePRogress)
    var s1 = Size(square.width, square.height * cos(theta))
    var l1 = Offset(leftCorner.x, c1.y - s1.height / 2)

    var centre = when {
        c1.y <= c2.y -> c1
        else -> c2
    }

    drawRect(
        color = colorLight,
        topLeft = centre,
        size = Size(square.width, (c1.y - c2.y).absoluteValue)
    )

    //top to bottom
    drawOval(
        color = colorLight,
        topLeft = l1,
        size = s1,
        style = Fill
    )

    //Oval from bottom to top
    if (!state.verticalfinish) {
        drawOval(
            color = colorLight,
            topLeft = l2,
            size = s2,
            style = Fill
        )
    }

    drawOval(
        color = colorDark,
        topLeft = l2,
        size = s2,
        style = Stroke(4f)
    )
}