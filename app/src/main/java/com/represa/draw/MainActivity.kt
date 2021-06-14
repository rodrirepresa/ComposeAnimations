package com.represa.draw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.represa.draw.ui.DatePicker

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                    updateStatusBar()
                    DatePicker()
                }

        }
    }

    @Composable
    fun updateStatusBar() {
        // Remember a SystemUiController
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !MaterialTheme.colors.isLight

        SideEffect {
            // Update all of the system bar colors to be transparent, and use
            // dark icons if we're in light theme
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )

            // setStatusBarsColor() and setNavigationBarsColor() also exist
        }
    }
}