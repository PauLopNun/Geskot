package com.geskot.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.geskot.app.data.model.ValenbisiStation
import com.geskot.app.presentation.screen.DetailScreen
import com.geskot.app.presentation.screen.MainScreen
import com.geskot.app.presentation.viewmodel.ValenbisiViewModel
import com.geskot.app.ui.theme.GesKotTheme

/**
 * Main Activity for the GesKot application
 * Sets up the main UI using Jetpack Compose and handles navigation
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GesKotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GesKotApp()
                }
            }
        }
    }
}

/**
 * Main app composable with navigation
 */
@Composable
fun GesKotApp() {
    val navController = rememberNavController()
    val viewModel: ValenbisiViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                onStationClick = { station ->
                    viewModel.selectStation(station)
                    navController.navigate("detail")
                },
                viewModel = viewModel
            )
        }

        composable("detail") {
            val selectedStation = viewModel.selectedStation.value
            selectedStation?.let { station ->
                DetailScreen(
                    station = station,
                    onBackClick = {
                        viewModel.clearSelectedStation()
                        navController.popBackStack()
                    }
                )
            } ?: run {
                // If no station is selected, navigate back to main
                navController.popBackStack()
            }
        }
    }
}