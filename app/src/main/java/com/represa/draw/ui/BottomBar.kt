package com.represa.draw.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun BottomBar() {

    var lastIndex = remember { mutableStateOf(0) }

    var items = listOf(
        "HOME",
        "NEW IN",
        "CLOTHING",
        "DENIM",
        "SHOES",
        "ACCESORIES",
        "SALE",
        "JUNIOR",
        "PLUS SIZE"
    )

    //Experiment with Density and Offset

    var e = with(LocalDensity.current) { 100.dp.toPx() }

    Box(modifier = Modifier
        .fillMaxSize()
        .drawBehind { drawCircle(Color.Blue, 3f, Offset(e, e)) }) {
        Box(modifier = Modifier.padding(100.dp, 100.dp)) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Red)
            )
        }

    }

    var state = rememberLazyListState()

    Column() {
        Text(
            text = """" 
                ${state.layoutInfo.viewportStartOffset}
                ${state.layoutInfo.viewportEndOffset}
            """.trimMargin()
        )

        state.layoutInfo.visibleItemsInfo.forEach {
            Text(
                text = """
            ${it.index} + "/" + ${it.offset} + "/" + ${it.size} + "///" + ${it.size + it.offset - 20}
        """.trimIndent()
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp)

        ) {
            var scope = rememberCoroutineScope()

            LazyRow(
                modifier = Modifier
                    .padding(0.dp, 8.dp),
                contentPadding = PaddingValues(10.dp, 2.dp),
                state = state
            ) {
                itemsIndexed(items) { index, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentWidth()
                            .padding(0.dp, 0.dp, 10.dp, 0.dp)
                            //.background(Color.Blue)
                            .clickable {
                                if (!state.isVisible(index)) {
                                    scope.launch {
                                        var scrollOffset =
                                            state.getItem(index).offset - (state.layoutInfo.viewportEndOffset / 2f) + state.getItem(
                                                index
                                            ).size / 2
                                        state.animateScrollBy(scrollOffset)
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item, modifier = Modifier
                                .padding(20.dp, 0.dp)
                        )
                    }
                }

            }

        }
    }

}

private fun LazyListState.getItem(index: Int): LazyListItemInfo {
    return layoutInfo.visibleItemsInfo.filter {
        it.index == index
    }.first()
}

private fun LazyListState.isVisible(index: Int): Boolean {
    layoutInfo.visibleItemsInfo.filter {
        it.index == index
    }.let {
        return@isVisible it.first().offset + it.first().size < layoutInfo.viewportEndOffset
    }
}