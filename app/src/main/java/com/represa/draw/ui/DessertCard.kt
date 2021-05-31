package com.represa.draw.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.represa.draw.data.Dessert

@Composable
fun DessertCard(dessert: Dessert) {
    Card(
        Modifier
            .padding(0.dp, 10.dp)
            .width(300.dp)
            .height(320.dp),
        elevation = 3.dp,
        shape = RoundedCornerShape(7.dp)
    ) {
        Column {
            Image(
                painter = rememberCoilPainter(
                    dessert.url,
                    fadeIn = true
                ),
                contentDescription = "f",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.height(200.dp)
            )
            Column(Modifier.padding(10.dp)) {
                Text(
                    text = dessert.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = dessert.description,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(0.dp, 10.dp),
                    color = Color.Gray
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(text = dessert.price)
                }
            }
        }
    }
}