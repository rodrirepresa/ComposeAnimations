package com.represa.draw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.LocalSystemUiController
import com.google.accompanist.systemuicontroller.rememberAndroidSystemUiController
import com.represa.draw.ui.theme.DrawTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Remember a controller, and provide it to the LocalSystemUiController
            val controller = rememberAndroidSystemUiController()
            CompositionLocalProvider(LocalSystemUiController provides controller) {
                Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                    updateStatusBar()
                    PhysicoChest()
                }
            }
        }
    }

    @Composable
    private fun updateStatusBar() {
        // Get the current SystemUiController
        val systemUiController = LocalSystemUiController.current

        SideEffect {
            // Update all of the system bar colors to be transparent, and use
            // dark icons if we're in light theme
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = false
            )

            // setStatusBarsColor() and setNavigationBarsColor() also exist
        }
    }
}