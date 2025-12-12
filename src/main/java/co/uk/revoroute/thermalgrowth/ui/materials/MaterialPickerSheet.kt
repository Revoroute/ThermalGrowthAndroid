package co.uk.revoroute.thermalgrowth.ui.materials

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.uk.revoroute.thermalgrowth.model.Material
import co.uk.revoroute.thermalgrowth.ui.calculator.CalculatorViewModel

import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// iOS category order
private val iosCategoryOrder = listOf(
    "Aluminium Alloys",
    "Steels",
    "Stainless Steels",
    "Non‑Ferrous Metals",
    "Polymers",
    "Specialty Materials"
)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MaterialPickerSheet(
    viewModel: CalculatorViewModel,
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

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                grouped.forEach { (category, materialList) ->

                    item {
                        Text(
                            text = category.uppercase(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 20.dp, top = 16.dp, bottom = 6.dp)
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
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = material.name,
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
    }
}