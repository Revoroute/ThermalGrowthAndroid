package co.uk.revoroute.thermalgrowth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import co.uk.revoroute.thermalgrowth.navigation.AppNavHost
import co.uk.revoroute.thermalgrowth.ui.splash.SplashOverlay
import co.uk.revoroute.thermalgrowth.ui.calculator.CalculatorViewModel
import co.uk.revoroute.thermalgrowth.ui.settings.SettingsViewModel
import co.uk.revoroute.thermalgrowth.data.MaterialRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsViewModel = SettingsViewModel(applicationContext)
        val materialRepository = MaterialRepository(applicationContext)
        val calculatorViewModel = CalculatorViewModel(materialRepository)

        setContent {
            MaterialTheme {
                var showSplash by remember { mutableStateOf(true) }

                val navController = rememberNavController()

                if (showSplash) {
                    SplashOverlay(onFinished = { showSplash = false })
                } else {
                    AppNavHost(
                        navController = navController,
                        settingsViewModel = settingsViewModel,
                        calculatorViewModel = calculatorViewModel
                    )
                }
            }
        }
    }
}