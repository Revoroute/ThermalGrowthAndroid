package co.uk.revoroute.thermalgrowth.ui.calculator

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalFocusManager
import co.uk.revoroute.thermalgrowth.ui.settings.SettingsViewModel
import co.uk.revoroute.thermalgrowth.model.Material
import co.uk.revoroute.thermalgrowth.ui.results.ResultCard
import co.uk.revoroute.thermalgrowth.ui.materials.MaterialPickerSheet
import co.uk.revoroute.thermalgrowth.ui.materials.TempPickerSheet
import co.uk.revoroute.thermalgrowth.ui.components.AdBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    settingsViewModel: SettingsViewModel,
    onOpenSettings: () -> Unit,
    onOpenInfo: () -> Unit
) {
    val measuredSize by viewModel.measuredSize.collectAsState()
    val measuredTemp by viewModel.measuredTemp.collectAsState()
    val selectedMaterial by viewModel.selectedMaterial.collectAsState()
    val calculationResult by viewModel.calculationResult.collectAsState()

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
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                actions = {
                    IconButton(onClick = onOpenInfo) {
                        Icon(Icons.Default.Info, contentDescription = "Info")
                    }
                }
            )
        },
        bottomBar = {
            AdBanner(
                adUnitId = "ca-app-pub-3940256099942544/6300978111",
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
                text = "Target size at reference temperature",
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
                        text = "Enter Size (mm)",
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
                            text = "Temperature (°C)",
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = measuredTemp.ifBlank { "Select" },
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
            startTemp = measuredTemp.toIntOrNull() ?: 20,
            onSelect = { temp ->
                viewModel.onMeasuredTempChanged(temp.toString())
                showTempSheet = false
            },
            onDismiss = { showTempSheet = false }
        )
    }
}