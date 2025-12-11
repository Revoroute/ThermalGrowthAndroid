package co.uk.revoroute.thermalgrowth.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A universal button used across the app.
 * Matches the iOS style:
 * - Accent color text
 * - Accent rounded rectangle border
 * - Transparent background
 */
@Composable
fun TGButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val accent = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(
                width = 1.dp,
                color = accent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.ClickableText(
            text = androidx.compose.ui.text.AnnotatedString(text),
            onClick = { onClick() },
            style = androidx.compose.ui.text.TextStyle(
                color = accent,
                fontSize = 18.sp
            )
        )
    }
}