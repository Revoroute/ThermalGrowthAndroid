package co.uk.revoroute.thermalgrowth.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold
    ),
    headlineMedium = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyLarge = TextStyle(
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp
    ),
    // For corrected values and formula breakdown
    labelLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 18.sp
    )
)