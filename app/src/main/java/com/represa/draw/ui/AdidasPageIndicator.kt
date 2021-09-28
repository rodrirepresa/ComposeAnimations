package com.represa.draw.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.represa.draw.extensions.notLower
import com.represa.draw.extensions.notOvertaking

@OptIn(ExperimentalPagerApi::class)
@Composable
@Preview
fun AdidasPageIndicator() {

    val pagerState = rememberPagerState(pageCount = IndicatorValue.itemCount)

    Box(modifier = Modifier.fillMaxSize()) {
        Background(pagerState = pagerState)
        IndicatorsContainer(pagerState = pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun IndicatorsContainer(pagerState: PagerState) {
    with(IndicatorValue) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingContainer)
                .height(heightContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("pageIndicatorContainer"),
                verticalAlignment = Alignment.Bottom
            ) {
                for (i in 0 until itemCount) {
                    SimpleIndicator(
                        modifier = Modifier
                            .weight(
                                calculateWeight(
                                    pagerState = pagerState,
                                    position = i
                                )
                            )
                            .testTag("indicator"),
                        color = color,
                        alpha = calculateAlpha(pagerState = pagerState, position = i),
                        height = calculateHeight(pagerState = pagerState, position = i),
                        paddingBetweenItems = paddingBetweenItems / 2
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleIndicator(
    modifier: Modifier,
    color: Color,
    alpha: Float,
    height: Dp,
    paddingBetweenItems: Dp
) {
    Canvas(
        modifier = modifier
            .fillMaxHeight()
            .padding(paddingBetweenItems, 0.dp)
    ) {
        // Translate the center to the left down corner
        translate(-size.width / 2, size.height / 2) {
            drawRect(
                color = color,
                alpha = alpha,
                topLeft = center,
                size = Size(size.width, -height.toPx())
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun calculateWeight(pagerState: PagerState, position: Int): Float {
    with(pagerState) {
        targetPage?.let {
            return when (position) {
                currentPage -> (2f * (1 - currentPageOffset.notOvertaking(1f))).notLower(1f)
                targetPage -> 1f + (1f * currentPageOffset.notOvertaking(1f))
                else -> 1f
            }
        } ?: return if (position == currentPage) 2f else 1f
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun calculateAlpha(
    pagerState: PagerState,
    position: Int,
    maxHeight: Dp = IndicatorValue.maxHeight / 2
): Float {
    return if (calculateHeight(
            pagerState = pagerState,
            position = position
        ) > maxHeight / 2
    ) 1f else 0.7f
}

@OptIn(ExperimentalPagerApi::class)
private fun calculateHeight(
    pagerState: PagerState,
    position: Int,
    maxHeight: Dp = IndicatorValue.maxHeight,
    minHeight: Dp = IndicatorValue.minHeight
): Dp {
    with(pagerState) {
        targetPage?.let { targetPage ->
            return when (position) {
                currentPage -> (maxHeight * (1 - currentPageOffset.notOvertaking(1f))).notLower(
                    minHeight
                )
                targetPage -> (maxHeight * currentPageOffset.notOvertaking(1f)).notLower(
                    minHeight
                )
                else -> minHeight
            }
        } ?: return if (pagerState.currentPage == position) maxHeight else minHeight
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Background(pagerState: PagerState) {

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.testTag("horizontalPager")
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),

                ) {
                Text(text = "SHOP THE LOOKBOOK", color = Color.White)
            }
        }
    }
}

object IndicatorValue {
    val color = Color.White
    val maxHeight = 4.dp
    val minHeight = 1.dp
    val paddingContainer = 14.dp
    val heightContainer = 20.dp
    var itemCount = 7
    val paddingBetweenItems = 4.dp
}