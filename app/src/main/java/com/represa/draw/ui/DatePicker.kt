package com.represa.draw.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@ExperimentalMaterialApi
@Composable
fun DatePicker() {
    var coroutineScope = rememberCoroutineScope()
    var calendarData = remember { CalendarData(coroutineScope) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BoxWithConstraints() {
        BottomSheetScaffold(scaffoldState = bottomSheetScaffoldState,
            sheetShape = RoundedCornerShape(20.dp),
            sheetPeekHeight = maxHeight * 0.7f,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(25.dp, 10.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Calendar(calendarData)
                }
            },
            modifier = Modifier.onGloballyPositioned {

            }) {
            var brush = Brush.sweepGradient(
                listOf(
                    Color.Blue,
                    Color.Cyan,
                    Color(0xFF66ffff),
                    Color(0xFF0099ff),
                    Color(0xFF0066ff),
                    Color.Blue
                )
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp, 0.dp)
                        .height(maxHeight * 0.28f),
                    verticalArrangement = Arrangement.Bottom
                ) {

                    Text(
                        text = "When do you want \nto travel?",
                        fontSize = 30.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

    }
}

@Composable
fun Calendar(calendarData: CalendarData) {
    Decoration()
    Box(Modifier.height(5.dp))
    Weeks(calendarData)
}

@Composable
fun Decoration() {
    Indicator()
    Month()
    WeekDays()
    Separator()
}

@Composable
fun Weeks(calendarData: CalendarData) {
    calendarData.apply {
        monthDays.keys.forEach { week ->
            Row() {
                monthDays[week]!!.forEach { day ->
                    Day(day, calendarData, Modifier.weight(1f))
                }
                var daysInWeek = monthDays.getValue(week).size
                if (daysInWeek < 7) {
                    Box(modifier = Modifier.weight((7 - daysInWeek).toFloat()))
                }
            }
        }
    }
}


@Composable
fun Day(day: Int, calendarData: CalendarData, modifier: Modifier) {

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
        modifier = modifier.height(40.dp),
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
                    drawRect(
                        color = Color.LightGray,
                        alpha = circleSize,
                        size = Size(size.width, size.height)
                    )
                }
        ) {
            Square(day, calendarData)
            Circle(day, calendarData)
            Text(
                text = day.toString(),
                color = if (calendarData.startDay == day || calendarData.endDay == day) Color.White else Color.Black,
                fontWeight = FontWeight.SemiBold
            )
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
                    color = Color.Black,
                    radius = circleSize * minOf(size.height, size.width) / 2,
                    center = center
                )

            }
            .clickable {
                calendarData.click(day)
            }) {
    }
}

@Composable
private fun Square(day: Int, calendarData: CalendarData) {
    var currentState by remember {
        mutableStateOf(SquareDayState.IDLE)
    }
    val transition = updateTransition(currentState, label = "2")

    val alpha by transition.animateFloat(
        transitionSpec = {
            when {
                SquareDayState.IDLE isTransitioningTo SquareDayState.Filled ->
                    tween(600)
                else ->
                    tween(500)
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
                    var border = calendarData.border(day)
                    if (border == BorderState.MIDDLE) {
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
                    } else {
                        var brush = if (border == BorderState.LEFT) {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.LightGray,
                                    Color.Transparent
                                ),
                                endX = 0f,
                                startX = size.width
                            )
                        } else {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.LightGray,
                                    Color.Transparent
                                )
                            )
                        }
                        drawRect(
                            brush = brush,
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
            }
    )
}


@Composable
fun Indicator() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .width(70.dp)
                .height(5.dp)
                .background(Color.LightGray)
        )
    }
}

@Composable
fun Month() {
    Text(
        text = "June 21",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(14.dp, 20.dp, 0.dp, 0.dp),
        fontSize = 18.sp
    )
}

@Composable
fun WeekDays() {
    var days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 10.dp)) {
        days.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun Separator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFEEEEEE))
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

enum class BorderState {
    MIDDLE, LEFT, RIGHT
}

class CalendarData(private val scope: CoroutineScope) {
    var filledDays = mutableStateListOf<Int>()
    var startDay by mutableStateOf<Int?>(null)
    var endDay by mutableStateOf<Int?>(null)
    var nextEndDay: Boolean = false
    var monthDays: Map<Int, List<Int>> = mapOf(
        (1 to listOf(1, 2, 3, 4, 5, 6, 7)),
        (2 to listOf(8, 9, 10, 11, 12, 13, 14)),
        (3 to listOf(15, 16, 17, 18, 19, 20, 21)),
        (4 to listOf(22, 23, 24, 25, 26, 27, 28)),
        (5 to listOf(29, 30, 31))
    )

    fun fill() {
        var list = mutableListOf<Int>()
        for (i in startDay!!..endDay!!) {
            list.add(i)
        }
        filledDays.addAll(list)
    }

    fun click(day: Int) {
        if (nextEndDay) {
            if (day > startDay!!) {
                endDay = day
                nextEndDay = false
                if (filledDays.isEmpty()) {
                    fill()
                }
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
        filledDays.removeRange(0, filledDays.size)
        nextEndDay = true
    }

    fun border(day: Int): BorderState {
        var border = BorderState.MIDDLE
        monthDays.keys.forEach { week ->
            if (monthDays.getValue(week).contains(day)) {
                if (monthDays.getValue(week).indexOf(day) == 0) {
                    border = BorderState.LEFT
                } else if (monthDays.getValue(week)
                        .indexOf(day) == monthDays.getValue(week).size - 1
                ) {
                    border = BorderState.RIGHT
                }
            }
        }
        return border
    }
}