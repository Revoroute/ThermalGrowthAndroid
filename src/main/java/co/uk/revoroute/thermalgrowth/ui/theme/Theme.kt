package co.uk.revoroute.thermalgrowth.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import co.uk.revoroute.thermalgrowth.app.AppSettingsStore

// --------------------------------------------------
// Colors
// --------------------------------------------------

// Accents
private val AccentBlue = Color(0xFF007AFF)
private val AccentOrange = Color(0xFFFF9500)
private val AccentGreen = Color(0xFF34C759)
private val AccentRed = Color(0xFFFF3B30)
private val AccentPurple = Color(0xFFAF52DE)

// Base
private val AppWhite = Color(0xFFFFFFFF)
private val AppBlack = Color(0xFF000000)

private val LightText = Color(0xFF1C1C1E)
private val DarkText = Color(0xFFE5E5EA)

private val LightSurface = Color(0xFFF2F2F7)
private val DarkSurface = Color(0xFF1C1C1E)

// --------------------------------------------------
// Typography
// --------------------------------------------------

private val AppTypography = Typography()

// --------------------------------------------------
// Color Schemes
// --------------------------------------------------

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
    surface = DarkSurface,
    onSurface = DarkText
)

// --------------------------------------------------
// Theme
// --------------------------------------------------

@Composable
fun ThermalGrowthTheme(
    settings: AppSettingsStore,
    content: @Composable () -> Unit
) {
    val theme = settings.theme.collectAsState().value
    val accent = settings.accent.collectAsState().value

    val accentColor = when (accent) {
        AppSettingsStore.AppAccent.BLUE -> AccentBlue
        AppSettingsStore.AppAccent.ORANGE -> AccentOrange
        AppSettingsStore.AppAccent.GREEN -> AccentGreen
        AppSettingsStore.AppAccent.RED -> AccentRed
        AppSettingsStore.AppAccent.PURPLE -> AccentPurple
    }

    val baseScheme = when (theme) {
        AppSettingsStore.AppTheme.LIGHT -> LightColors
        AppSettingsStore.AppTheme.DARK -> DarkColors
        AppSettingsStore.AppTheme.HIGH_CONTRAST -> darkColorScheme(
            primary = Color.Yellow,
            onPrimary = Color.Black,
            background = Color.Black,
            onBackground = Color.White,
            surface = Color.Black,
            onSurface = Color.White
        )
        AppSettingsStore.AppTheme.SYSTEM -> DarkColors
    }

    MaterialTheme(
        colorScheme = baseScheme.copy(primary = accentColor),
        typography = AppTypography,
        content = content
    )
}