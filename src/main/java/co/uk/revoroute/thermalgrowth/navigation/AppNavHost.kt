package co.uk.revoroute.thermalgrowth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.uk.revoroute.thermalgrowth.ui.calculator.CalculatorScreen
import co.uk.revoroute.thermalgrowth.ui.settings.SettingsScreen
import co.uk.revoroute.thermalgrowth.ui.info.InfoScreen
import co.uk.revoroute.thermalgrowth.viewmodel.CalculatorViewModel
import co.uk.revoroute.thermalgrowth.ui.settings.SettingsViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    calculatorViewModel: CalculatorViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Calculator.route,
        modifier = modifier
    ) {

        composable(NavigationRoute.Calculator.route) {
            CalculatorScreen(
                viewModel = calculatorViewModel,
                settingsViewModel = settingsViewModel,
                onOpenSettings = { navController.navigate(NavigationRoute.Settings.route) },
                onOpenInfo = { navController.navigate(NavigationRoute.Info.route) }
            )
        }

        composable(NavigationRoute.Settings.route) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavigationRoute.Info.route) {
            InfoScreen(
                viewModel = calculatorViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}