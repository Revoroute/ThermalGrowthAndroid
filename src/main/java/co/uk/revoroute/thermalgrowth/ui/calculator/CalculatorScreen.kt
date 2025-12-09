package co.uk.revoroute.thermalgrowth.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val calculationResult by viewModel.calculationResult.collectAsState()
    val showResultSheet by viewModel.showResultSheet.collectAsState()

    var showMaterialSheet by remember { mutableStateOf(false) }
    var showTempSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thermal Growth", fontSize = 20.sp) },
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            OutlinedTextField(
                value = measuredSize,
                onValueChange = { viewModel.onMeasuredSizeChanged(it) },
                label = { Text("Measured Size (mm)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Material", color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        selectedMaterial?.name ?: "Select",
                        color = MaterialTheme.colorScheme.primary
                    )
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Temperature (°C)", color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        measuredTemp.ifBlank { "Select" },
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Button(
                onClick = { viewModel.calculate() },
                enabled = viewModel.isCalculateEnabled(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Calculate", fontSize = 18.sp)
            }

            if (calculationResult != null) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Corrected Size",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = String.format("%.5f mm", calculationResult!!.correctedSize),
                            fontSize = 28.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Expansion: ${String.format("%.5f", calculationResult!!.expansionAmount)} mm",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = calculationResult!!.breakdown,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }

    if (showMaterialSheet) {
        MaterialPickerSheet(
            viewModel = viewModel,
            onDismiss = { showMaterialSheet = false },
            onSelect = {
                viewModel.onMaterialSelected(it)
                showMaterialSheet = false
            }
        )
    }

    if (showTempSheet) {
        ModalBottomSheet(onDismissRequest = { showTempSheet = false }) {
            Column(Modifier.padding(16.dp)) {
                (0..600).forEach { temp ->
                    Text(
                        text = "$temp°C",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clickable {
                                viewModel.onMeasuredTempChanged(temp.toString())
                                showTempSheet = false
                            }
                    )
                }
            }
        }
    }

    if (showResultSheet) {
        ModalBottomSheet(onDismissRequest = { viewModel.dismissResultSheet() }) {
            val result = calculationResult ?: return@ModalBottomSheet
            Column(Modifier.padding(20.dp)) {
                Text("Corrected: ${result.correctedSize}")
                Text("Expansion: ${result.expansionAmount}")
                Text(result.breakdown)
            }
        }
    }
}