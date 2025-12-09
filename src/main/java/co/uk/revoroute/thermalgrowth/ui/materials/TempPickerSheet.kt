package co.uk.revoroute.thermalgrowth.ui.materials

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TempPickerSheet(
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit,
    startTemp: Int = 20 // default like iOS
) {
    val temps = remember { (-150..600).toList() }
    val initialIndex = temps.indexOf(startTemp).coerceAtLeast(0)

    val listState = rememberLazyListState(initialIndex)
    val fling = rememberSnapFlingBehavior(listState)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        tonalElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {

            // Highlight bar (iOS style)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                        shape = MaterialTheme.shapes.medium
                    )
            )

            // Temperature wheel
            LazyColumn(
                state = listState,
                flingBehavior = fling,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                itemsIndexed(temps) { index, temp ->

                    val center = listState.layoutInfo.visibleItemsInfo
                        .firstOrNull { it.index == index }?.offset ?: 0

                    // Distance from center for fading
                    val distance = kotlin.math.abs(center)
                    val fade = when {
                        distance < 20 -> 1f
                        distance < 80 -> 0.6f
                        else -> 0.3f
                    }

                    // Selected?
                    val isSelected =
                        listState.firstVisibleItemIndex == index ||
                                listState.firstVisibleItemScrollOffset in 0..20 &&
                                listState.firstVisibleItemIndex + 1 == index

                    Text(
                        text = "$temp°C",
                        fontSize = if (isSelected) 26.sp else 20.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = fade),
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .alpha(fade)
                    )
                }
            }

            // Confirm selection on stop
            LaunchedEffect(listState.isScrollInProgress) {
                if (!listState.isScrollInProgress) {
                    val centerIndex = listState.firstVisibleItemIndex
                    val selectedTemp = temps.getOrNull(centerIndex)
                    if (selectedTemp != null) {
                        onSelect(selectedTemp)
                    }
                }
            }
        }
    }
}