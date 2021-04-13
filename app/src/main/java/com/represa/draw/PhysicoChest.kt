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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*

@Composable
fun PhysicoChest() {
    var scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Chest(scope)
    }
}

private val colorBackground = Color(0xFFB2EBF2)
private val colorSquare = Color(0xFF263238)
private val square = Size(100f, 100f)

class ChestState(private val scope: CoroutineScope) {
    var topLeftProgressHorizontal = Animatable(initialValue = 0f)
    var ovalSizeProgressHorizontal = Animatable(initialValue = -90f)
    var topLeftProgressVertical = Animatable(initialValue = 0f)
    var ovalSizeProgressVertical = Animatable(initialValue = -90f)
    var color = mutableStateOf(colorBackground)
    var drawHorizontal = mutableStateOf(true)
    var drawVertical = mutableStateOf(false)
    var horizontalfinish = mutableStateOf(false)
    var verticalfinish = mutableStateOf(true)

    private val delay = 3000
    private val delay_stop = 1000

    init {
        startHorizontal()
        startVertical()
    }

    private fun startHorizontal() {
        scope.launch {
            while (true) {
                delay(1000)
                drawVertical.value = false
                color.value = colorBackground
                ovalSizeProgressHorizontal.snapTo(-90f)
                horizontalfinish.value = false
                ovalSizeProgressHorizontal.animateTo(
                    targetValue = 90f,
                    animationSpec = tween(durationMillis = delay, easing = LinearEasing)
                )
                horizontalfinish.value = true
                delay(delay_stop.toLong())
                drawHorizontal.value = false
                delay(delay.toLong())
            }
        }
        scope.launch {
            while (true) {
                delay(delay_stop.toLong())
                topLeftProgressHorizontal.snapTo(0f)
                topLeftProgressHorizontal.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = delay, easing = LinearEasing)
                )
                delay(delay_stop.toLong())
                delay(delay.toLong())
            }
        }
    }

    private fun startVertical() {
        scope.launch {
            while (true) {
                drawHorizontal.value = true
                delay(delay.toLong() + 2*delay_stop.toLong())
                color.value = colorSquare
                drawVertical.value = true
                ovalSizeProgressVertical.snapTo(-90f)
                ovalSizeProgressVertical.animateTo(
                    targetValue = 90f,
                    animationSpec = tween(durationMillis = delay, easing = LinearEasing)
                )

            }
        }
        scope.launch {
            while (true) {
                delay(delay.toLong() + 2*delay_stop.toLong())
                verticalfinish.value = false
                topLeftProgressVertical.snapTo(0f)
                topLeftProgressVertical.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = delay, easing = LinearEasing)
                )
                verticalfinish.value = true
            }
        }
    }
}


@Composable
private fun Chest(scope: CoroutineScope) {

    var state = remember { ChestState(scope) }

    Box(
        Modifier
            .fillMaxSize()
            .background(state.color.value)
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
                                r % 2 == 1 && c % 2 == 1 && state.drawHorizontal.value -> {
                                    horizontalLeft(state, position)
                                }
                                r % 2 == 1 && c % 2 == 0 && state.drawVertical.value -> {
                                    verticalDown(state, position)
                                }
                                r % 2 == 0 && c % 2 == 1 && state.drawVertical.value -> {
                                    verticalUp(state, position)
                                }
                                r % 2 == 0 && c % 2 == 0 && state.drawHorizontal.value -> {
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
        color = colorSquare,
        topLeft = l2,
        size = s2,
        style = Fill
    )

    var centre = when {
        c1.x <= c2.x -> c1
        else -> c2
    }

    drawRect(
        color = colorSquare,
        topLeft = centre,
        size = Size((c1.x - c2.x).absoluteValue, square.height)
    )

    //left to right
    if (!state.horizontalfinish.value) {
        drawOval(
            color = colorBackground,
            topLeft = l1,
            size = s1,
            style = Stroke(4f)
        )
    }

    drawOval(
        color = colorSquare,
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
        color = colorSquare,
        topLeft = l1,
        size = s1,
        style = Fill
    )

    var centre = when {
        c1.x <= c2.x -> c1
        else -> c2
    }

    drawRect(
        color = colorSquare,
        topLeft = centre,
        size = Size((c1.x - c2.x).absoluteValue, square.height)
    )

    //right to left
    if (!state.horizontalfinish.value) {
        drawOval(
            color = colorBackground,
            topLeft = l2,
            size = s2,
            style = Stroke(4f)
        )
    }

    drawOval(
        color = colorSquare,
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
        color = colorBackground,
        topLeft = l2,
        size = s2,
        style = Fill
    )

    var centre = when {
        c1.y <= c2.y -> c1
        else -> c2
    }

    drawRect(
        color = colorBackground,
        topLeft = centre,
        size = Size(square.width, (c1.y - c2.y).absoluteValue)
    )

    //Oval from top to bottom
    if (!state.verticalfinish.value) {
        drawOval(
            color = colorSquare,
            topLeft = l1,
            size = s1,
            style = Stroke(4f)
        )
    }

    drawOval(
        color = colorBackground,
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
        color = colorBackground,
        topLeft = centre,
        size = Size(square.width, (c1.y - c2.y).absoluteValue)
    )

    //top to bottom
    drawOval(
        color = colorBackground,
        topLeft = l1,
        size = s1,
        style = Fill
    )

    //Oval from bottom to top
    if (!state.verticalfinish.value) {
        drawOval(
            color = colorBackground,
            topLeft = l2,
            size = s2,
            style = Fill
        )
    }

    drawOval(
        color = colorSquare,
        topLeft = l2,
        size = s2,
        style = Stroke(4f)
    )
}