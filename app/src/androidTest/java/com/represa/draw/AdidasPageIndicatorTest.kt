package com.represa.draw

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.represa.draw.ui.AdidasPageIndicator
import com.represa.draw.ui.IndicatorValue
import com.represa.draw.ui.theme.DrawTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AdidasPageIndicatorTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    private val indicatorsValue = IndicatorValue

    @Before
    fun beforeTest() {
        composeTestRule.setContent {
            DrawTheme {
                AdidasPageIndicator()
            }
        }
    }


    @Test
    fun spawnCorrectIndicators() {

        val pageIndicatorContainer = composeTestRule.onNodeWithTag("pageIndicatorContainer")
        val pageIndicator = composeTestRule.onAllNodes(matcher = hasTestTag("indicator"))
        pageIndicator.assertCountEquals(indicatorsValue.itemCount)
        assert(pageIndicatorContainer.fetchSemanticsNode().children.size == indicatorsValue.itemCount)

        val firstIndicatorWidth = pageIndicator.onFirst().fetchSemanticsNode().layoutInfo.width
        for (i in 1 until indicatorsValue.itemCount) {
            pageIndicatorContainer.onChildAt(i).apply {
                assert(fetchSemanticsNode().layoutInfo.width != firstIndicatorWidth)
            }
        }
    }

    @Test
    fun allChildWidthShouldBeEqualToParentWidth() {

        val pageIndicator = composeTestRule.onAllNodes(matcher = hasTestTag("indicator"))
        var totalWidth = 0

        for (i in 0 until indicatorsValue.itemCount) {
            totalWidth += pageIndicator[i].fetchSemanticsNode().layoutInfo.width
        }
    }

    @Test
    fun selectedWidthShouldBeDouble() {

        val pageIndicatorContainer = composeTestRule.onNodeWithTag("pageIndicatorContainer")
        val layoutWidth = pageIndicatorContainer.fetchSemanticsNode().layoutInfo.width
        val notSelectedIndicatorWidth = layoutWidth / (indicatorsValue.itemCount + 1)
        val selectedIndicatorWidth = (layoutWidth / (indicatorsValue.itemCount + 1)) * 2

        pageIndicatorContainer.onChildAt(0).apply {
            assert(fetchSemanticsNode().layoutInfo.width in (selectedIndicatorWidth..selectedIndicatorWidth + 2))
        }

        for (i in 1 until indicatorsValue.itemCount) {
            pageIndicatorContainer.onChildAt(i).apply {
                assert(fetchSemanticsNode().layoutInfo.width in (notSelectedIndicatorWidth..notSelectedIndicatorWidth + 1))
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun afterScroll() {

        val pageIndicatorContainer = composeTestRule.onNodeWithTag("pageIndicatorContainer")
        val layoutWidth = pageIndicatorContainer.fetchSemanticsNode().layoutInfo.width
        var notSelectedIndicatorWidth = layoutWidth / (indicatorsValue.itemCount + 1)
        var selectedIndicatorWidth = (layoutWidth / (indicatorsValue.itemCount + 1)) * 2

        for (i in 0 until indicatorsValue.itemCount) {
            if (i == 0) {
                pageIndicatorContainer.onChildAt(i).apply {
                    assert(fetchSemanticsNode().layoutInfo.width in (selectedIndicatorWidth..selectedIndicatorWidth + 2))
                }
            } else {
                pageIndicatorContainer.onChildAt(i).apply {
                    assert(fetchSemanticsNode().layoutInfo.width in (notSelectedIndicatorWidth..notSelectedIndicatorWidth + 1))
                }
            }
        }
        if (indicatorsValue.itemCount > 1) {

            composeTestRule.onNode(hasTestTag("horizontalPager"), useUnmergedTree = true)
                .performGesture { swipeLeft(durationMillis = 50000L) }

            for (i in 0 until indicatorsValue.itemCount) {
                if (i == 1) {
                    pageIndicatorContainer.onChildAt(i).apply {
                        assert(fetchSemanticsNode().layoutInfo.width in (selectedIndicatorWidth..selectedIndicatorWidth + 2))
                    }
                } else {
                    pageIndicatorContainer.onChildAt(i).apply {
                        assert(fetchSemanticsNode().layoutInfo.width in (notSelectedIndicatorWidth..notSelectedIndicatorWidth + 1))
                    }
                }
            }
        }
    }
}