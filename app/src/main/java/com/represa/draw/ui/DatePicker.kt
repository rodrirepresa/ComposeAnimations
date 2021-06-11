package com.represa.draw.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun DatePicker() {
    var coroutineScope = rememberCoroutineScope()
    var calendarData = remember { CalendarData(coroutineScope) }

    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        ModalBottomSheetValue.HalfExpanded
    )

    ModalBottomSheetLayout(sheetState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(20.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(25.dp, 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Indicator()
                WeekDays()
                Separator()
                Row() {
                    Day(1, calendarData, Modifier.weight(1f))
                    Day(2, calendarData, Modifier.weight(1f))
                    Day(3, calendarData, Modifier.weight(1f))
                    Day(4, calendarData, Modifier.weight(1f))
                    Day(5, calendarData, Modifier.weight(1f))
                    Day(6, calendarData, Modifier.weight(1f))
                    Day(7, calendarData, Modifier.weight(1f))
                }
                Row() {
                    Day(8, calendarData, Modifier.weight(1f))
                    Day(9, calendarData, Modifier.weight(1f))
                    Day(10, calendarData, Modifier.weight(1f))
                    Day(11, calendarData, Modifier.weight(1f))
                    Day(12, calendarData, Modifier.weight(1f))
                    Day(13, calendarData, Modifier.weight(1f))
                    Day(14, calendarData, Modifier.weight(1f))
                }
                Row() {
                    Day(15, calendarData, Modifier.weight(1f))
                    Day(16, calendarData, Modifier.weight(1f))
                    Day(17, calendarData, Modifier.weight(1f))
                    Day(18, calendarData, Modifier.weight(1f))
                    Day(19, calendarData, Modifier.weight(1f))
                    Day(20, calendarData, Modifier.weight(1f))
                    Day(21, calendarData, Modifier.weight(1f))
                }
                Row() {
                    Day(22, calendarData, Modifier.weight(1f))
                    Day(23, calendarData, Modifier.weight(1f))
                    Day(24, calendarData, Modifier.weight(1f))
                    Day(25, calendarData, Modifier.weight(1f))
                    Day(26, calendarData, Modifier.weight(1f))
                    Day(27, calendarData, Modifier.weight(1f))
                    Day(28, calendarData, Modifier.weight(1f))
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
        }) {

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
        modifier = modifier.height(50.dp),
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
            Text(
                text = day.toString(),
                color = if (calendarData.startDay == day || calendarData.endDay == day) Color.White else Color.Black
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
                    radius = circleSize * size.width / 2,
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
                    tween(500)
                else ->
                    tween(650)
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

@Composable
fun WeekDays() {
    Row(modifier = Modifier.padding(0.dp, 40.dp, 0.dp, 10.dp)) {
        Text(text = "Sun", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "Mon", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "Tue", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "Wed", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "Thu", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "Fri", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "Sat", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
    }
}

@Composable
fun Separator(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Color(0xFFEEEEEE)))
}

@Composable
fun Indicator(){
    Box(modifier = Modifier
        .clip(CircleShape)
        .width(70.dp)
        .height(5.dp)
        .background(Color.LightGray))
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
    var filledDays = mutableStateListOf<Int>()
    var startDay by mutableStateOf<Int?>(null)
    var endDay by mutableStateOf<Int?>(null)
    var nextEndDay: Boolean = false
    var squareSpeed: Int = 50

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
                if(filledDays.isEmpty()) {
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
        squareSpeed = 50
    }
}