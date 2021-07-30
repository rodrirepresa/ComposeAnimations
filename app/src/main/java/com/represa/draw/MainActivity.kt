package com.represa.draw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.represa.draw.ui.BottomBar
import com.represa.draw.ui.DatePicker
import com.represa.draw.ui.RoundIndicators
import com.represa.draw.ui.SplashScreen

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                MainScreen()
            }

        }
    }

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @Composable
    fun MainScreen() {

        updateStatusBar()
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "mainScreen",
            modifier = Modifier
        ) {
            composable("mainScreen") { Buttons(navController) }
            composable("bottomBar") { BottomBar() }
            composable("datePicker") { DatePicker() }
            composable("indicators") { DotsIndicator() }
            composable("roundIndicators") { RoundIndicators() }
            composable("adidasSplash") { SplashScreen {} }
            composable("trippyChest") { PhysicoChest() }
        }


    }

    @Composable
    fun Buttons(navController: NavController) {
        var modifier = Modifier.padding(0.dp, 20.dp)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(onClick = { navController.navigate("bottomBar") }, modifier = modifier) {
                Text(text = "BottomBar")
            }
            Button(onClick = { navController.navigate("datePicker") }, modifier = modifier) {
                Text(text = "DatePicker")
            }
            Button(onClick = { navController.navigate("indicators") }, modifier = modifier) {
                Text(text = "Indicators")
            }
            Button(onClick = { navController.navigate("roundIndicators") }, modifier = modifier) {
                Text(text = "RoundIndicators")
            }
            Button(onClick = { navController.navigate("adidasSplash") }, modifier = modifier) {
                Text(text = "AdidasSplash")
            }
            Button(onClick = { navController.navigate("trippyChest") }, modifier = modifier) {
                Text(text = "TrippyChest")
            }
        }
    }

    @Composable
    fun updateStatusBar() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !MaterialTheme.colors.isLight

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
    }
}