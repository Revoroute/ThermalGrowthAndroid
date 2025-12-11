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

// iOS category order
private val iosCategoryOrder = listOf(
    "Aluminium Alloys",
    "Steels",
    "Stainless Steels",
    "Tool Steels",
    "Titanium Alloys",
    "Copper Alloys",
    "Brass Alloys",
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
                .padding(bottom = 32.dp)
        ) {
            grouped.forEach { (category, materialList) ->

                // Category header
                Text(
                    text = category.uppercase(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 16.dp, bottom = 6.dp)
                )

                // Material rows
                materialList.forEach { material ->

                    val isSelected = selectedMaterial?.name == material.name

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(material)
                                onDismiss()
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

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}