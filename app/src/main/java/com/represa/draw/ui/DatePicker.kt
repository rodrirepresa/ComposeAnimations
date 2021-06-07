package com.represa.draw.ui

import android.widget.Toast
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
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
            Day(1, calendarData.daysFilled)
            Day(2, calendarData.daysFilled)
            Day(3, calendarData.daysFilled)
            Day(4, calendarData.daysFilled)
            Day(5, calendarData.daysFilled)
            Day(6, calendarData.daysFilled)
            Day(7, calendarData.daysFilled)

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
fun Day(day: Int, filledDays: List<Int?>) {


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

            Rect(day, filledDays)
            Circle()
            Text(text = day.toString(), color = Color.Red)
        }
    }
}

@Composable
fun Circle() {
    var currentState by remember {
        mutableStateOf(DayState.IDLE)
    }
    val transition = updateTransition(currentState, label = "")

    val circleSize by transition.animateFloat(
        transitionSpec = {
            when {
                DayState.IDLE isTransitioningTo DayState.Selected ->
                    spring(stiffness = Spring.StiffnessLow)
                else ->
                    spring(stiffness = Spring.StiffnessLow)
            }
        }
    ) { state ->
        when (state) {
            DayState.IDLE -> 0f
            DayState.Selected -> 1f
        }
    }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawCircle(
                    color = Color.Black,
                    radius = circleSize * size.height / 2,
                    center = center
                )

            }
            .clickable {
                currentState = if (currentState == DayState.IDLE) {
                    DayState.Selected
                } else {
                    DayState.IDLE
                }
            }) {
    }
}

@Composable
private fun Rect(day: Int, filledDays: List<Int?>) {
    var currentState2 by remember {
        mutableStateOf(DayState2.IDLE)
    }
    val transition2 = updateTransition(currentState2, label = "2")

    val filledSize by transition2.animateFloat(
        transitionSpec = {
            when {
                DayState2.IDLE isTransitioningTo DayState2.Filled ->
                    tween(50, easing = LinearEasing)
                else ->
                    tween(50)
            }
        }
    ) { state ->
        when (state) {
            DayState2.IDLE -> 0f
            DayState2.Filled -> 1f
        }
    }

    if (filledDays.contains(day)) {
        currentState2 = DayState2.Filled
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {


                drawRect(Color.Cyan, size = Size(filledSize * size.width, size.height))

            })

}


private enum class DayState {
    Selected,
    IDLE
}

private enum class DayState2 {
    Filled,
    IDLE
}

class CalendarData(private val scope: CoroutineScope) {
    var daysFilled by mutableStateOf(listOf<Int?>(null))

    fun fill() {
        scope.launch {
            var list = daysFilled.toMutableList()
            list.add(1)
            daysFilled = list.toList()
            delay(50)
            list = daysFilled.toMutableList()
            list.add(2)
            daysFilled = list.toList()
        }
    }
}