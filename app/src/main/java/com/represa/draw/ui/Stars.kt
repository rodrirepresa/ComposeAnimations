package com.represa.draw.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Stars
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun rate() {
    var starData = remember { StarData() }
    Row(Modifier.fillMaxSize()) {
        for (i in 1..5) {
            star(position = i, starData = starData)
        }
    }
}

@Composable
fun star(position: Int, starData: StarData) {

    var scale = remember { Animatable(1f) }
    var coroutineScope = rememberCoroutineScope()

    var showFilled = starData.starList.contains(position)


    Box() {
        Icon(Icons.Default.StarOutline, contentDescription = "", modifier = Modifier
            .scale(scale.value)
            .clickable {
                starData.click(position)
                starData.click(position)
                coroutineScope.launch {
                    scale.animateTo(
                        targetValue = 1.7f,
                        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                    )
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                    )
                }
            })

        if (showFilled) {
            Icon(Icons.Default.Star, contentDescription = "", modifier = Modifier
                .scale(scale.value)
                .clickable {
                    starData.click(position)
                    coroutineScope.launch {
                        scale.animateTo(
                            targetValue = 1.7f,
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearEasing
                            )
                        )
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearEasing
                            )
                        )
                    }
                }
            )
        }
    }
}


class StarData {
    var starList = mutableStateListOf<Int>()

    fun click(star: Int) {
        var list = mutableListOf<Int>()
        for (i in 1..star) {
            list.add(i)
        }
        starList.clear()
        starList.addAll(list)
    }
}