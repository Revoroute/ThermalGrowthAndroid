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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalFocusManager
import co.uk.revoroute.thermalgrowth.app.AppSettingsStore
import co.uk.revoroute.thermalgrowth.ui.results.ResultCard
import co.uk.revoroute.thermalgrowth.ui.components.MaterialPickerSheet
import co.uk.revoroute.thermalgrowth.ui.components.TempPickerSheet
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
    val referenceTempC by settings.referenceTempC.collectAsState()

    // Display helper for measured temperature, shows in selected unit but keeps state in Celsius
    val displayMeasuredTemp = remember(measuredTemp, unitSystem) {
        val tempC = measuredTemp.toIntOrNull()
        if (tempC == null) {
            ""
        } else if (unitSystem == AppSettingsStore.UnitSystem.IMPERIAL) {
            "${(tempC * 9 / 5) + 32}°F"
        } else {
            "$tempC°C"
        }
    }

    val focusManager = LocalFocusManager.current

    var showMaterialSheet by remember { mutableStateOf(false) }
    var showTempSheet by remember { mutableStateOf(false) }

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
            AdBanner(
                adUnitId = "ca-app-pub-3557221706331893/4654583309",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
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

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTempSheet = true }
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
                            text = settings.displayTemperatureLabel(),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = displayMeasuredTemp.ifBlank { "Select" },
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

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

    if (showTempSheet) {
        TempPickerSheet(
            startTemp = measuredTemp.toIntOrNull() ?: referenceTempC,
            unitSystem = unitSystem,
            onSelect = { temp ->
                viewModel.onMeasuredTempChanged(temp.toString())
                showTempSheet = false
            },
            onDismiss = { showTempSheet = false }
        )
    }
}