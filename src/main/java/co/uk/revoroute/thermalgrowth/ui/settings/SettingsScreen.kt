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
import co.uk.revoroute.thermalgrowth.BuildConfig
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Star
import co.uk.revoroute.thermalgrowth.app.AppSettingsStore

import co.uk.revoroute.thermalgrowth.ui.components.TempPickerSheet

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButtonDefaults

import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.ReviewInfo
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettingsStore,
    onBack: () -> Unit
) {
    val accent by settings.accent.collectAsState()
    val unitSystem by settings.unitSystem.collectAsState()
    val theme by settings.theme.collectAsState()
    val referenceTempC by settings.referenceTempC.collectAsState()

    LaunchedEffect(unitSystem) {
        // Force re‑emit of reference temperature when units change
        settings.setReferenceTempC(referenceTempC)
    }

    val context = LocalContext.current

    var showReferenceTempPicker by remember { mutableStateOf(false) }

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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // THEME
            SettingsSection(title = "Theme") {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SegmentedButton(
                        selected = theme == AppSettingsStore.AppTheme.SYSTEM,
                        onClick = { settings.setTheme(AppSettingsStore.AppTheme.SYSTEM) },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                    ) {
                        Text("System")
                    }

                    SegmentedButton(
                        selected = theme == AppSettingsStore.AppTheme.LIGHT,
                        onClick = { settings.setTheme(AppSettingsStore.AppTheme.LIGHT) },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                    ) {
                        Text("Light")
                    }

                    SegmentedButton(
                        selected = theme == AppSettingsStore.AppTheme.DARK,
                        onClick = { settings.setTheme(AppSettingsStore.AppTheme.DARK) },
                        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                    ) {
                        Text("Dark")
                    }
                }
            }

            // ACCENT COLOR
            SettingsSection(title = "Accent Color") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppSettingsStore.AppAccent.values().forEach { color ->
                        val selected = (color == accent)

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = when (color) {
                                        AppSettingsStore.AppAccent.BLUE -> Color(0xFF007AFF)
                                        AppSettingsStore.AppAccent.ORANGE -> Color(0xFFFF9500)
                                        AppSettingsStore.AppAccent.GREEN -> Color(0xFF34C759)
                                        AppSettingsStore.AppAccent.RED -> Color(0xFFFF3B30)
                                        AppSettingsStore.AppAccent.PURPLE -> Color(0xFFAF52DE)
                                    },
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { settings.setAccent(color) }
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
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SegmentedButton(
                        selected = unitSystem == AppSettingsStore.UnitSystem.METRIC,
                        onClick = { settings.setUnitSystem(AppSettingsStore.UnitSystem.METRIC) },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) {
                        Text("Metric")
                    }

                    SegmentedButton(
                        selected = unitSystem == AppSettingsStore.UnitSystem.IMPERIAL,
                        onClick = { settings.setUnitSystem(AppSettingsStore.UnitSystem.IMPERIAL) },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) {
                        Text("Imperial")
                    }
                }
            }

            // REFERENCE TEMP
            SettingsSection(title = "Reference Temperature") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showReferenceTempPicker = true }
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reference temperature",
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = settings.displayReferenceTemperature(),
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // RATE APP
            SettingsSection(title = "Feedback") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            val activity = context as? Activity ?: return@Button

                            val manager = ReviewManagerFactory.create(activity)
                            val request = manager.requestReviewFlow()

                            request.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val reviewInfo: ReviewInfo = task.result
                                    manager.launchReviewFlow(activity, reviewInfo)
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null
                            )
                            Text(
                                text = "Rate this app",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
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

        // Show TempPickerSheet if requested
        if (showReferenceTempPicker) {
            TempPickerSheet(
                startTemp = referenceTempC,
                unitSystem = unitSystem,
                onSelect = { temp ->
                    settings.setReferenceTempC(temp)
                    showReferenceTempPicker = false
                },
                onDismiss = {
                    showReferenceTempPicker = false
                }
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
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
fun SettingsOptionRow(
    title: String,
    selected: Boolean,
    accentColor: Color,
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
                tint = accentColor
            )
        }
        }
    }