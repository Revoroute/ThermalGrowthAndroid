package co.uk.revoroute.thermalgrowth.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import co.uk.revoroute.thermalgrowth.BuildConfig
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val accent by settingsViewModel.accent.collectAsState()
    val units by settingsViewModel.units.collectAsState()
    val theme by settingsViewModel.theme.collectAsState()
    val referenceTemp by settingsViewModel.referenceTemp.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ACCENT COLOR
            SettingsSection(title = "Accent Color") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SettingsViewModel.AppAccent.values().forEach { color ->
                        val selected = (color == accent)

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = when (color) {
                                        SettingsViewModel.AppAccent.BLUE -> Color(0xFF007AFF)
                                        SettingsViewModel.AppAccent.ORANGE -> Color(0xFFFF9500)
                                        SettingsViewModel.AppAccent.GREEN -> Color(0xFF34C759)
                                        SettingsViewModel.AppAccent.RED -> Color(0xFFFF3B30)
                                        SettingsViewModel.AppAccent.PURPLE -> Color(0xFFAF52DE)
                                    },
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { settingsViewModel.setAccent(color) }
                                .then(
                                    if (selected) Modifier.border(
                                        width = 3.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(20.dp)
                                    ) else Modifier
                                )
                        )
                    }
                }
            }

            // UNITS
            SettingsSection(title = "Units") {
                SettingsOptionRow(
                    title = "Celsius (mm/°C)",
                    selected = units == SettingsViewModel.UnitPreference.CELSIUS,
                    onClick = { settingsViewModel.setUnits(SettingsViewModel.UnitPreference.CELSIUS) }
                )
                SettingsOptionRow(
                    title = "Fahrenheit (inch/°F)",
                    selected = units == SettingsViewModel.UnitPreference.FAHRENHEIT,
                    onClick = { settingsViewModel.setUnits(SettingsViewModel.UnitPreference.FAHRENHEIT) }
                )
            }

            // THEME
            SettingsSection(title = "Theme") {
                SettingsOptionRow(
                    title = "System Default",
                    selected = theme == SettingsViewModel.AppTheme.SYSTEM,
                    onClick = { settingsViewModel.setTheme(SettingsViewModel.AppTheme.SYSTEM) }
                )
                SettingsOptionRow(
                    title = "Light",
                    selected = theme == SettingsViewModel.AppTheme.LIGHT,
                    onClick = { settingsViewModel.setTheme(SettingsViewModel.AppTheme.LIGHT) }
                )
                SettingsOptionRow(
                    title = "Dark",
                    selected = theme == SettingsViewModel.AppTheme.DARK,
                    onClick = { settingsViewModel.setTheme(SettingsViewModel.AppTheme.DARK) }
                )
                SettingsOptionRow(
                    title = "High Contrast",
                    selected = theme == SettingsViewModel.AppTheme.HIGH_CONTRAST,
                    onClick = { settingsViewModel.setTheme(SettingsViewModel.AppTheme.HIGH_CONTRAST) }
                )
            }

            // REFERENCE TEMP
            SettingsSection(title = "Reference Temperature") {
                SettingsOptionRow(
                    title = "$referenceTemp°C",
                    selected = true,
                    onClick = {
                        // TODO: Open reference temperature picker
                    }
                )
            }

            Spacer(Modifier.height(20.dp))

            // VERSION FOOTER
            Text(
                text = "Version ${BuildConfig.VERSION_NAME}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        content()
    }
}

@Composable
fun SettingsOptionRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}