package co.uk.revoroute.thermalgrowth.ui.materials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row

@OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class
)
@Composable
fun TempPickerSheet(
    startTemp: Int = 20,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val temps = remember { (600 downTo -150).toList() }
    var selected by remember { mutableStateOf(startTemp) }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = temps.indexOf(startTemp).coerceAtLeast(0)
    )

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
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(temps) { temp ->

                    val isSelected = temp == selected

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selected = temp }
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$temp°C",
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
    }
}