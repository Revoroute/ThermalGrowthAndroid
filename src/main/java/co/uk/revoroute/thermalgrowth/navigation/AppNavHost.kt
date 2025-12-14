package co.uk.revoroute.thermalgrowth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.uk.revoroute.thermalgrowth.ui.calculator.CalculatorScreen
import co.uk.revoroute.thermalgrowth.ui.settings.SettingsScreen
import co.uk.revoroute.thermalgrowth.ui.info.InfoScreen
import co.uk.revoroute.thermalgrowth.ui.calculator.CalculatorState
import co.uk.revoroute.thermalgrowth.app.AppSettingsStore

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    settings: AppSettingsStore,
    calculatorViewModel: CalculatorState
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Calculator.route,
        modifier = modifier
    ) {

        composable(NavigationRoute.Calculator.route) {
            CalculatorScreen(
                viewModel = calculatorViewModel,
                settings = settings,
                onOpenSettings = { navController.navigate(NavigationRoute.Settings.route) },
                onOpenInfo = { navController.navigate(NavigationRoute.Info.route) }
            )
        }

        composable(NavigationRoute.Settings.route) {
            SettingsScreen(
                settings = settings,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavigationRoute.Info.route) {
            InfoScreen(
                viewModel = calculatorViewModel,
                settings = settings,
                onBack = { navController.popBackStack() }
            )
        }
    }
}