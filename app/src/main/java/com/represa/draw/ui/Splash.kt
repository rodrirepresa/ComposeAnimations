package com.represa.draw.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun SplashScreen(onSucces: () -> Unit) {

    var sizeLine = remember{ Animatable(0f) }

    LaunchedEffect(sizeLine){
        sizeLine.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000)
        )
        onSucces.invoke()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .drawBehind {
            drawLine(
                strokeWidth = 25f,
                color = Color.White,
                start = Offset(size.width / 2, 0f),
                end = Offset(size.width / 2, size.height * sizeLine.value)
            )
            drawLine(
                strokeWidth = 25f,
                color = Color.White,
                start = Offset(size.width / 2 - 80, 0f),
                end = Offset(size.width / 2 - 80, size.height * sizeLine.value)
            )
            drawLine(
                strokeWidth = 25f,
                color = Color.White,
                start = Offset(size.width / 2 + 80, 0f),
                end = Offset(size.width / 2 + 80, size.height * sizeLine.value)
            )
        }) {
    }
}