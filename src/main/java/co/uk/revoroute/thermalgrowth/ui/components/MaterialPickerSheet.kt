package co.uk.revoroute.thermalgrowth.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.uk.revoroute.thermalgrowth.model.Material
import co.uk.revoroute.thermalgrowth.ui.calculator.CalculatorState

import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api

// iOS category order
private val iosCategoryOrder = listOf(
    "Aluminium Alloys",
    "Carbides",
    "Carbon & Graphite",
    "Ceramics & Glass",
    "Non-Ferrous Metals",
    "Polymers",
    "Stainless Steels",
    "Steels"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialPickerSheet(
    viewModel: CalculatorState,
    onDismiss: () -> Unit,
    onSelect: (Material) -> Unit
) {
    val materials = viewModel.materials.collectAsState().value
    val selectedMaterial = viewModel.selectedMaterial.collectAsState().value

    var selected by remember(selectedMaterial) { mutableStateOf(selectedMaterial) }

    // Group materials by category in iOS order
    val grouped = iosCategoryOrder.associateWith { category ->
        materials.filter { it.category == category }
    }.filterValues { it.isNotEmpty() }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        tonalElevation = 3.dp
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp),
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                Button(
                    onClick = {
                        selected?.let { onSelect(it) }
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Done", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                grouped.forEach { (category, materialList) ->

                    item {
                        Text(
                            text = category.uppercase(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 6.dp)
                        )
                    }

                    items(materialList) { material ->

                        val isSelected = selected?.name == material.name

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selected = material
                                }
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = material.name,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 17.sp,
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}