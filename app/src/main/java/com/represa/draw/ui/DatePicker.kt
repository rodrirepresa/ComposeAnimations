package com.represa.draw.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DatePicker() {
    var coroutineScope = rememberCoroutineScope()
    var calendarData = remember { CalendarData(coroutineScope) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFCFF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row() {
            Day(1, calendarData)
            Day(2, calendarData)
            Day(3, calendarData)
            Day(4, calendarData)
            Day(5, calendarData)
            Day(6, calendarData)
            Day(7, calendarData)
        }
        Row(Modifier.fillMaxWidth().height(20.dp)) {

        }
        Row() {
            Day(8, calendarData)
            Day(9, calendarData)
            Day(10, calendarData)
            Day(11, calendarData)
            Day(12, calendarData)
            Day(13, calendarData)
            Day(14, calendarData)
        }
        Row(Modifier.fillMaxWidth().height(20.dp)) {

        }
        Row() {
            Day(15, calendarData)
            Day(16, calendarData)
            Day(17, calendarData)
            Day(18, calendarData)
            Day(19, calendarData)
            Day(20, calendarData)
            Day(21, calendarData)
        }
        Row(Modifier.fillMaxWidth().height(20.dp)) {

        }
        Row() {
            Day(22, calendarData)
            Day(23, calendarData)
            Day(24, calendarData)
            Day(25, calendarData)
            Day(26, calendarData)
            Day(27, calendarData)
            Day(28, calendarData)
        }

        /*Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(Modifier.size(350.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row() {
                        for (i in 1..7) {
                            Day(i, calendarData)
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)) {

                    }
                    Row() {
                        for (i in 8..14) {
                            Day(i, calendarData)
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)) {

                    }
                    Row() {
                        for (i in 15..21) {
                            Day(i, calendarData)
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)) {

                    }
                    Row() {
                        for (i in 22..28) {
                            Day(i, calendarData)
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)) {

                    }
                }
            }
        }*/
    }

}


@Composable
fun Day(day: Int, calendarData: CalendarData) {

    var currentState by remember {
        mutableStateOf(CircleDayState.IDLE)
    }
    val transition = updateTransition(currentState, label = "")

    val circleSize by transition.animateFloat(
        transitionSpec = {
            when {
                CircleDayState.IDLE isTransitioningTo CircleDayState.Selected ->
                    tween(1000)
                else ->
                    tween(1000)
            }
        }
    ) { state ->
        when (state) {
            CircleDayState.IDLE -> 0f
            CircleDayState.Selected -> 0.4f
        }
    }

    Card(
        modifier = Modifier.size(50.dp),
        elevation = 0.dp,
        shape = RoundedCornerShape(0.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    currentState = if (currentState == CircleDayState.IDLE) {
                        CircleDayState.Selected
                    } else {
                        CircleDayState.IDLE
                    }
                }
                .drawBehind {
                   /* var brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.LightGray,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent
                        ), tileMode = TileMode.Repeated
                    )
                    var brush2 = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.LightGray
                        ), tileMode = TileMode.Repeated
                    )
                    drawRect(
                        brush = brush,
                        size = Size(size.width, size.height / 2),
                        alpha = circleSize
                    )
                    drawRect(
                        topLeft = Offset(0f, size.height / 2),
                        brush = brush2,
                        size = Size(size.width, size.height),
                        alpha = circleSize
                    )*/
                    drawRect(
                        color = Color.LightGray,
                        alpha = circleSize,
                        size = Size(size.width, size.height)
                    )
                }
        ) {

            //Rect(day, calendarData)
            SquareTest(day, calendarData)
            Circle(day, calendarData)
            Text(text = day.toString(), color = Color.Red)
        }
    }
}


@Composable
fun Circle(day: Int, calendarData: CalendarData) {
    var currentState by remember {
        mutableStateOf(CircleDayState.IDLE)
    }
    val transition = updateTransition(currentState, label = "")

    val circleSize by transition.animateFloat(
        transitionSpec = {
            when {
                CircleDayState.IDLE isTransitioningTo CircleDayState.Selected ->
                    spring(stiffness = 600f)
                else ->
                    spring(stiffness = 600f)
            }
        }
    ) { state ->
        when (state) {
            CircleDayState.IDLE -> 0f
            CircleDayState.Selected -> 1f
        }
    }

    if (calendarData.startDay == day || calendarData.endDay == day) {
        currentState = CircleDayState.Selected
    } else if (calendarData.startDay != day && calendarData.endDay != day) {
        currentState = CircleDayState.IDLE
    }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawCircle(
                    color = if (calendarData.startDay == day) Color.Green else Color.Black,
                    radius = circleSize * size.height / 2,
                    center = center
                )

            }
            .clickable {
                calendarData.click(day)
            }) {
    }
}

@Composable
private fun SquareTest(day: Int, calendarData: CalendarData) {
    var currentState by remember {
        mutableStateOf(SquareDayState.IDLE)
    }
    val transition = updateTransition(currentState, label = "2")

    val alpha by transition.animateFloat(
        transitionSpec = {
            when {
                SquareDayState.IDLE isTransitioningTo SquareDayState.Filled ->
                    tween(1000)
                else ->
                    tween(1000)
            }
        }
    ) { state ->
        when (state) {
            SquareDayState.IDLE -> 0f
            SquareDayState.Filled -> 0.4f
        }
    }

    currentState = if (calendarData.filledDays.contains(day)) {
        SquareDayState.Filled
    } else {
        SquareDayState.IDLE
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                if (calendarData.filledDays.contains(day)) {
                    drawRect(
                        color = Color.LightGray,
                        alpha = alpha,
                        size = if (calendarData.startDay == day || calendarData.endDay == day) {
                            Size(size.width / 2, size.height)
                        } else {
                            Size(size.width, size.height)
                        },
                        topLeft = if (calendarData.startDay == day) {
                            Offset(size.width / 2, 0f)
                        } else {
                            Offset.Zero
                        }
                    )
                }
            }
    )
}

@Composable
private fun Rect(day: Int, calendarData: CalendarData) {
    var currentState by remember {
        mutableStateOf(SquareDayState.IDLE)
    }
    val transition = updateTransition(currentState, label = "2")

    val filledSize by transition.animateFloat(
        transitionSpec = {
            when {
                SquareDayState.IDLE isTransitioningTo SquareDayState.Filled ->
                    tween(calendarData.squareSpeed, easing = LinearEasing)
                else ->
                    snap()
            }
        }
    ) { state ->
        when (state) {
            SquareDayState.IDLE -> 0f
            SquareDayState.Filled -> 1f
        }
    }

    currentState = if (calendarData.filledDays.contains(day)) {
        SquareDayState.Filled
    } else {
        SquareDayState.IDLE
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                if (calendarData.filledDays.contains(day)) {
                    drawRect(
                        color = Color.Cyan,
                        size = if (calendarData.startDay == day || calendarData.endDay == day) {
                            Size(filledSize * size.width / 2, size.height)
                        } else {
                            Size(filledSize * size.width, size.height)
                        },
                        topLeft = if (calendarData.startDay == day) {
                            Offset(size.width / 2, 0f)
                        } else {
                            Offset.Zero
                        }
                    )
                }
            }
    )

}


private enum class CircleDayState {
    Selected,
    IDLE
}

private enum class SquareDayState {
    Filled,
    IDLE
}

class CalendarData(private val scope: CoroutineScope) {
    var filledDays by mutableStateOf(setOf<Int?>(null))
    var startDay by mutableStateOf<Int?>(null)
    var endDay by mutableStateOf<Int?>(null)
    var nextEndDay: Boolean = false
    var squareSpeed: Int = 50
    val MAX_SPEED = 200

    fun fill() {
        scope.launch {
            //squareSpeed = MAX_SPEED / (endDay!! - startDay!!)
            var list = filledDays.toMutableList()
            for (i in startDay!!..endDay!!) {
                list.add(i)
                //delay(squareSpeed.toLong())
            }
            filledDays = list.toSet()
        }
    }

    fun click(day: Int) {
        if (nextEndDay) {
            if (day > startDay!!) {
                endDay = day
                nextEndDay = false
                fill()
            } else {
                startDay(day)
            }
        } else {
            startDay(day)
        }
    }

    fun startDay(day: Int) {
        startDay = day
        endDay = null
        filledDays = setOf(null)
        nextEndDay = true
        squareSpeed = 50
    }
}