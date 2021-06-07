package com.represa.draw.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Size
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
        Button(onClick = {
            calendarData.fill()
        }) {

        }
        /*
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(Modifier.size(300.dp)) {
                    Column(modifier = Modifier
                        .fillMaxSize()) {
                        Row() {
                            for (i in 1..7) {
                                Day(i)
                            }
                        }
                        Row(Modifier.fillMaxWidth().height(40.dp)) {

                        }
                        Row() {
                            for (i in 8..14) {
                                Day(i)
                            }
                        }
                        Row(Modifier.fillMaxWidth().height(40.dp)) {

                        }
                        Row() {
                            for (i in 15..21) {
                                Day(i)
                            }
                        }
                        Row(Modifier.fillMaxWidth().height(40.dp)) {

                        }
                        Row() {
                            for (i in 22..28) {
                                Day(i)
                            }
                        }
                        Row(Modifier.fillMaxWidth().height(40.dp)) {

                        }
                    }
                }
            }*/
    }

}


@Composable
fun Day(day: Int, calendarData: CalendarData) {

    Card(
        modifier = Modifier.size(50.dp),
        elevation = 0.dp,
        shape = RoundedCornerShape(0.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Rect(day, calendarData.filledDays)
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
                    spring(stiffness = Spring.StiffnessLow)
                else ->
                    spring(stiffness = Spring.StiffnessLow)
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
private fun Rect(day: Int, filledDays: Set<Int?>) {
    var currentState by remember {
        mutableStateOf(SquareDayState.IDLE)
    }
    val transition = updateTransition(currentState, label = "2")

    val filledSize by transition.animateFloat(
        transitionSpec = {
            when {
                SquareDayState.IDLE isTransitioningTo SquareDayState.Filled ->
                    tween(50, easing = LinearEasing)
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

    currentState = if (filledDays.contains(day)) {
        SquareDayState.Filled
    }else{
        SquareDayState.IDLE
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {


                drawRect(Color.Cyan, size = Size(filledSize * size.width, size.height))

            })

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

    fun fill() {
        scope.launch {
            var start = startDay!! + 1
            for (i in start until endDay!!) {
                var list = filledDays.toMutableList()
                if (!list.contains(i)) {
                    list.add(i)
                }
                filledDays = list.toSet()
                delay(50)
            }
        }
    }

    fun click(day: Int) {
        if (nextEndDay) {
            endDay = day
            nextEndDay = false
            fill()
        } else {
            startDay = day
            endDay = null
            filledDays = setOf(null)
            nextEndDay = true
        }
    }
}