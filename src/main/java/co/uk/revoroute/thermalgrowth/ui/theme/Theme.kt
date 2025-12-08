package co.uk.revoroute.thermalgrowth.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import co.uk.revoroute.thermalgrowth.ui.settings.SettingsViewModel

private val LightColors = lightColorScheme(
    primary = AccentBlue,
    onPrimary = AppWhite,
    background = AppWhite,
    onBackground = LightText,
    surface = LightSurface,
    onSurface = LightText
)

private val DarkColors = darkColorScheme(
    primary = AccentBlue,
    onPrimary = AppBlack,
    background = AppBlack,
    onBackground = DarkText,
    surface = DarkCard,
    onSurface = DarkText
)

@Composable
fun ThermalGrowthTheme(
    settingsViewModel: SettingsViewModel,
    content: @Composable () -> Unit
) {
    val theme = settingsViewModel.theme.collectAsState().value

    val colorScheme = when (theme) {
        SettingsViewModel.AppTheme.LIGHT -> LightColors
        SettingsViewModel.AppTheme.DARK -> DarkColors

        SettingsViewModel.AppTheme.HIGH_CONTRAST -> darkColorScheme(
            primary = Color.Yellow,
            onPrimary = Color.Black,
            background = Color.Black,
            onBackground = Color.White,
            surface = Color.Black,
            onSurface = Color.White
        )

        SettingsViewModel.AppTheme.SYSTEM -> {
            // System dark follows your rule: DARK IS PRIMARY
            DarkColors
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}