package co.uk.revoroute.thermalgrowth

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import co.uk.revoroute.thermalgrowth.navigation.AppNavHost
import co.uk.revoroute.thermalgrowth.ui.splash.SplashOverlay
import co.uk.revoroute.thermalgrowth.ui.calculator.CalculatorState
import co.uk.revoroute.thermalgrowth.app.AppSettingsStore
import co.uk.revoroute.thermalgrowth.app.AppSettingsStoreFactory
import co.uk.revoroute.thermalgrowth.ui.calculator.CalculatorStateFactory
import co.uk.revoroute.thermalgrowth.ui.theme.ThermalGrowthTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android 15+ edge-to-edge compliance
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            val settings: AppSettingsStore = viewModel(
                factory = AppSettingsStoreFactory(applicationContext)
            )

            ThermalGrowthTheme(settings) {

                val calculatorViewModel: CalculatorState = viewModel(
                    factory = CalculatorStateFactory(
                        context = applicationContext,
                        settings = settings
                    )
                )

                var showSplash by remember { mutableStateOf(true) }
                val navController = rememberNavController()

                Box {
                    // Always render the app content first
                    AppNavHost(
                        navController = navController,
                        settings = settings,
                        calculatorViewModel = calculatorViewModel
                    )

                    // Draw splash ON TOP and animate it away
                    if (showSplash) {
                        SplashOverlay(
                            onFinished = { showSplash = false }
                        )
                    }
                }
            }
        }
    }
}