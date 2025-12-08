package co.uk.revoroute.thermalgrowth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import co.uk.revoroute.thermalgrowth.navigation.AppNavHost
import co.uk.revoroute.thermalgrowth.ui.splash.SplashOverlay
import co.uk.revoroute.thermalgrowth.viewmodel.CalculatorViewModel
import co.uk.revoroute.thermalgrowth.ui.settings.SettingsViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsViewModel = SettingsViewModel(applicationContext)
        val calculatorViewModel = CalculatorViewModel(settingsViewModel)

        setContent {
            val navController = rememberNavController()

            // Use your theme later — for now Material3 adaptive
            MaterialTheme {

                Box {
                    // Full Navigation System
                    AppNavHost(
                        navController = navController,
                        settingsViewModel = settingsViewModel,
                        calculatorViewModel = calculatorViewModel
                    )

                    // Splash overlay matches iOS animation logic
                    SplashOverlay()
                }
            }
        }
    }
}