package co.uk.revoroute.thermalgrowth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
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

                if (showSplash) {
                    SplashOverlay(onFinished = { showSplash = false })
                } else {
                    AppNavHost(
                        navController = navController,
                        settings = settings,
                        calculatorViewModel = calculatorViewModel
                    )
                }
            }
        }
    }
}