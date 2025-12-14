package co.uk.revoroute.thermalgrowth.ui.info

import co.uk.revoroute.thermalgrowth.BuildConfig

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import co.uk.revoroute.thermalgrowth.model.Material
import co.uk.revoroute.thermalgrowth.ui.calculator.CalculatorState
import co.uk.revoroute.thermalgrowth.app.AppSettingsStore

import kotlin.math.round

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    viewModel: CalculatorState,
    settings: AppSettingsStore,
    onBack: () -> Unit
) {
    val materials = viewModel.materials.collectAsState().value

    val unitSystem by settings.unitSystem.collectAsState()

    val groupedMaterials = materials
        .groupBy { it.category }
        .toSortedMap()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            item {
                Text(
                    text = "Calculator Info",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text =
                        "This calculator corrects a measured size at temperature T back to your selected reference temperature using:\n\n" +
                        "Lᵣ = Lₜ × [1 + α × (T − R)]\n\n" +
                        "Where Lᵣ is the corrected length at the reference temperature R (set in Settings), " +
                        "Lₜ is the measured length, α is the material’s thermal expansion coefficient, " +
                        "and T is the measurement temperature.",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            groupedMaterials.forEach { (category, items) ->

                // For Aluminium Alloys, show the CTE units row above the header
                if (category == "Aluminium Alloys") {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "",
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (unitSystem == AppSettingsStore.UnitSystem.METRIC)
                                    "CTE (×10⁻⁶ / °C)"
                                else
                                    "CTE (×10⁻⁶ / °F)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = category,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp)
                    )
                }

                items(items) { material ->
                    MaterialInfoRow(
                        material = material,
                        unitSystem = unitSystem
                    )
                }

                // Divider between material groups
                item {
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                val context = LocalContext.current

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            val subject = Uri.encode("Thermal Growth – Material request / feedback")

                            val body = Uri.encode(
                                """
                                App: Thermal Growth (Android)
                                Version: ${BuildConfig.VERSION_NAME}
                                Units: ${unitSystem.name}
                                """.trimIndent()
                            )

                            val intent = Intent(
                                Intent.ACTION_SENDTO,
                                Uri.parse("mailto:appsupport@revoroute.co.uk?subject=$subject&body=$body")
                            )
                            context.startActivity(intent)
                        },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = androidx.compose.ui.graphics.Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium
                        )
                    ) {
                        Text(
                            text = "Request a material / Send feedback",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun MaterialInfoRow(
    material: Material,
    unitSystem: AppSettingsStore.UnitSystem
) {
    val displayedAlpha = if (unitSystem == AppSettingsStore.UnitSystem.IMPERIAL) {
        material.alpha / 1.8
    } else {
        material.alpha
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = material.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = String.format("%.1f", displayedAlpha),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}