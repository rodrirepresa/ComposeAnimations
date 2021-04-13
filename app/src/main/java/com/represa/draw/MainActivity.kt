package com.represa.draw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.represa.draw.ui.theme.DrawTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrawTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                    PhysicoChest()
                }
            }
        }
    }
}