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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.layout.Spacer

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

    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val filtered = remember(materials, query) {
        val q = query.trim()
        if (q.isEmpty()) materials
        else materials.filter { it.name.contains(q, ignoreCase = true) }
    }

    val grouped = remember(filtered) {
        filtered
            .groupBy { it.category }
            .toSortedMap() // alphabetical category order
            .mapValues { (_, items) -> items.sortedBy { it.name } }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                singleLine = true,
                placeholder = { Text("Search materials") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                grouped.forEach { (category, materialList) ->
                    item {
                        Text(
                            text = category,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 6.dp)
                        )
                    }

                    items(materialList) { material ->
                        val isSelected = selected?.name == material.name

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selected = material
                                    onSelect(material)
                                    onDismiss()
                                }
                                .padding(horizontal = 4.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = material.name,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                fontSize = 17.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Spacer replacing the old Done button area
                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
    }
}