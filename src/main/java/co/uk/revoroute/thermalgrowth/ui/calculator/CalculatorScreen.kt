package co.uk.revoroute.thermalgrowth.ui.calculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.uk.revoroute.thermalgrowth.ui.settings.SettingsViewModel
import co.uk.revoroute.thermalgrowth.model.Material

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
    val showResultSheet by viewModel.showResultSheet.collectAsState()

    var showMaterialSheet by remember { mutableStateOf(false) }
    var showTempSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thermal Growth") },
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
            // Placeholder for AdMob banner component
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Ad Banner Placeholder")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Measured size input ---
            OutlinedTextField(
                value = measuredSize,
                onValueChange = { viewModel.onMeasuredSizeChanged(it) },
                label = { Text("Measured Size (mm)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // --- Material picker ---
            Button(
                onClick = { showMaterialSheet = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedMaterial?.name ?: "Select Material")
            }

            // --- Temperature input/picker ---
            Button(
                onClick = { showTempSheet = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Temperature (°C): $measuredTemp")
            }

            // --- Calculate button ---
            Button(
                onClick = { viewModel.calculate() },
                enabled = viewModel.isCalculateEnabled(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate")
            }
        }
    }

    // --- SHEETS ---

    if (showMaterialSheet) {
        MaterialPickerSheet(
            materials = viewModel.materials.collectAsState().value,
            onSelect = { material ->
                viewModel.onMaterialSelected(material)
                showMaterialSheet = false
            },
            onDismiss = { showMaterialSheet = false }
        )
    }

    if (showTempSheet) {
        TempPickerSheet(
            onSelect = { temp ->
                viewModel.onMeasuredTempChanged(temp.toString())
                showTempSheet = false
            },
            onDismiss = { showTempSheet = false }
        )
    }

    if (showResultSheet) {
        ResultsScreen(
            viewModel = viewModel,
            onDismiss = { viewModel.dismissResultSheet() }
        )
    }
}

/* ------------------------------------------------------------
   TEMPORARY COMPOSABLES so this file compiles cleanly.
   These will be replaced during 4.3, 4.4, and 4.5.
------------------------------------------------------------- */

@Composable
fun MaterialPickerSheet(
    materials: List<Material>,
    onSelect: (Material) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            materials.forEach {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .wrapContentHeight()
                        .clickable {
                            onSelect(it)
                        }
                )
            }
        }
    }
}

@Composable
fun TempPickerSheet(
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            (0..100).forEach { temp ->
                Text(
                    text = "$temp°C",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable { onSelect(temp) }
                )
            }
        }
    }
}

@Composable
fun ResultsScreen(
    viewModel: CalculatorViewModel,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            Text("Corrected: ...")
            Text("Expansion: ...")
            Text("Formula: ...")
        }
    }
}