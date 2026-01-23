package co.uk.revoroute.thermalgrowth.ui.calculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalFocusManager
import co.uk.revoroute.thermalgrowth.app.AppSettingsStore
import co.uk.revoroute.thermalgrowth.ui.results.ResultCard
import co.uk.revoroute.thermalgrowth.ui.components.MaterialPickerSheet
import co.uk.revoroute.thermalgrowth.ui.components.AdBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorState,
    settings: AppSettingsStore,
    onOpenSettings: () -> Unit,
    onOpenInfo: () -> Unit
) {
    val measuredSize by viewModel.measuredSize.collectAsState()
    val measuredTemp by viewModel.measuredTemp.collectAsState()
    val selectedMaterial by viewModel.selectedMaterial.collectAsState()
    val calculationResult by viewModel.calculationResult.collectAsState()

    val unitSystem by settings.unitSystem.collectAsState()

    val focusManager = LocalFocusManager.current

    var showMaterialSheet by remember { mutableStateOf(false) }

    var isTempFocused by remember { mutableStateOf(false) }

    // Temperature input (typed). Stored in the VM as Celsius, displayed/entered in the selected unit system.
    var isTempNegative by remember { mutableStateOf(false) }
    var tempMagnitudeText by remember { mutableStateOf("") }

    fun formatTrim(value: Double, maxDecimals: Int): String {
        val s = String.format(java.util.Locale.US, "%1$.${maxDecimals}f", value)
        return s.trimEnd('0').trimEnd('.')
    }

    // Keep the input fields in sync when measuredTemp or unit system changes (e.g., on load or settings change)
    LaunchedEffect(measuredTemp, unitSystem, isTempFocused) {
        if (isTempFocused) return@LaunchedEffect

        val tempC = measuredTemp.toDoubleOrNull()
        if (tempC == null) {
            isTempNegative = false
            tempMagnitudeText = ""
        } else {
            val display = if (unitSystem == AppSettingsStore.UnitSystem.IMPERIAL) (tempC * 9.0 / 5.0) + 32.0 else tempC
            isTempNegative = display < 0
            val mag = kotlin.math.abs(display)
            // display up to 2dp, trimmed
            tempMagnitudeText = formatTrim(mag, 2)
        }
    }

    fun commitTempToViewModel() {
        if (tempMagnitudeText.isBlank()) {
            viewModel.onMeasuredTempChanged("")
            return
        }
        val mag = tempMagnitudeText.toDoubleOrNull() ?: return

        val signedDisplay = if (isTempNegative) -mag else mag

        val minDisplay = if (unitSystem == AppSettingsStore.UnitSystem.IMPERIAL) -459.4 else -273.0
        val maxDisplay = if (unitSystem == AppSettingsStore.UnitSystem.IMPERIAL) 4532.0 else 2500.0
        if (signedDisplay !in minDisplay..maxDisplay) return

        val tempC = if (unitSystem == AppSettingsStore.UnitSystem.IMPERIAL) {
            (signedDisplay - 32.0) * 5.0 / 9.0
        } else {
            signedDisplay
        }

        // Store with higher precision to avoid imperial round-trip artefacts (e.g. 20°F -> 19.99°F)
        viewModel.onMeasuredTempChanged(formatTrim(tempC, 6))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Thermal Growth",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpenInfo) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                AdBanner(
                    adUnitId = "ca-app-pub-3557221706331893/4654583309",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Target size at ${settings.displayReferenceTemperature()} reference",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = measuredSize,
                onValueChange = { viewModel.onMeasuredSizeChanged(it) },
                placeholder = {
                    Text(
                        text = settings.displayInputSizeHint(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showMaterialSheet = true }
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Material",
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = selectedMaterial?.name ?: "Select",
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Temperature (typed)
            OutlinedTextField(
                value = tempMagnitudeText,
                onValueChange = { newValue ->
                    // allow digits and a single dot
                    val filtered = newValue.filter { it.isDigit() || it == '.' }
                    // prevent more than one dot
                    val normalized = if (filtered.count { it == '.' } > 1) {
                        val firstDot = filtered.indexOf('.')
                        val head = filtered.take(firstDot + 1)
                        val tail = filtered.drop(firstDot + 1).replace(".", "")
                        head + tail
                    } else filtered

                    tempMagnitudeText = normalized
                    commitTempToViewModel()
                },
                placeholder = {
                    Text(
                        text = "Enter Temperature",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                leadingIcon = {
                    Box(
                        modifier = Modifier.width(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = {
                                isTempNegative = !isTempNegative
                                commitTempToViewModel()
                            }
                        ) {
                            Text(
                                text = if (isTempNegative) "−" else "+",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                trailingIcon = {
                    Box(
                        modifier = Modifier.width(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (unitSystem == AppSettingsStore.UnitSystem.IMPERIAL) "°F" else "°C",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isTempFocused = it.isFocused }
            )

            val result = calculationResult
            if (result != null) {
                ResultCard(
                    result = result,
                    settings = settings,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                Text(
                    text = "Enter a size to calculate",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }

    if (showMaterialSheet) {
        MaterialPickerSheet(
            viewModel = viewModel,
            onDismiss = { showMaterialSheet = false },
            onSelect = { material ->
                viewModel.onMaterialSelected(material)
                showMaterialSheet = false
            }
        )
    }
}