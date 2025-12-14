package co.uk.revoroute.thermalgrowth.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.uk.revoroute.thermalgrowth.app.AppSettingsStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempPickerSheet(
    startTemp: Int = 20,
    unitSystem: AppSettingsStore.UnitSystem,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val temps = remember { (600 downTo -150).toList() }
    var selected by remember { mutableStateOf(startTemp) }

    val unitLabel =
        if (unitSystem == AppSettingsStore.UnitSystem.METRIC) "°C" else "°F"

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = temps.indexOf(startTemp).coerceAtLeast(0)
    )

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
                        onSelect(selected)
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
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                items(temps) { temp ->
                    val isSelected = temp == selected

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selected = temp }
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val displayTemp =
                            if (unitSystem == AppSettingsStore.UnitSystem.IMPERIAL)
                                (temp * 9 / 5) + 32
                            else
                                temp

                        Text(
                            text = "$displayTemp$unitLabel",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 17.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
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