package co.uk.revoroute.thermalgrowth.model

import java.text.DecimalFormat

object CalculationEngine {

    private const val DEFAULT_REFERENCE_TEMP = 20.0

    // Main calculation function
    fun calculateCorrectedSize(
        measuredSize: Double,
        materialAlpha: Double,
        measuredTemp: Double,
        referenceTemp: Double = DEFAULT_REFERENCE_TEMP
    ): CalculationResult {

        // Convert alpha (e.g. 11.5) to coefficient (11.5 × 1e-6)
        val alphaCoefficient = materialAlpha * 1e-6

        val deltaTemp = measuredTemp - referenceTemp
        val corrected = measuredSize * (1 + (alphaCoefficient * deltaTemp))
        val expansion = corrected - measuredSize

        val breakdown = buildBreakdownString(
            measuredSize,
            alphaCoefficient,
            measuredTemp,
            referenceTemp,
            corrected
        )

        return CalculationResult(
            correctedSize = corrected,
            expansionAmount = expansion,
            breakdown = breakdown
        )
    }

    // Create the breakdown string for the results screen
    private fun buildBreakdownString(
        measuredSize: Double,
        alphaCoefficient: Double,
        tempMeasured: Double,
        tempRef: Double,
        corrected: Double
    ): String {

        val df = DecimalFormat("0.000000")
        val alphaFormatted = DecimalFormat("0.0000000").format(alphaCoefficient)

        return "${df.format(measuredSize)} × [1 + $alphaFormatted × (${df.format(tempMeasured)} − ${df.format(tempRef)})] = ${df.format(corrected)}"
    }
}

// Data holder for calculation results
data class CalculationResult(
    val correctedSize: Double,
    val expansionAmount: Double,
    val breakdown: String
)