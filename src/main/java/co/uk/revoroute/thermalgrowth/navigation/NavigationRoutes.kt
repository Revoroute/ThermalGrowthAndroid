package co.uk.revoroute.thermalgrowth.navigation

sealed class NavigationRoute(val route: String) {
    object Calculator : NavigationRoute("calculator")
    object Settings : NavigationRoute("settings")
    object Info : NavigationRoute("info")
}