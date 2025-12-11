package co.uk.revoroute.thermalgrowth.ui.calculator

/**
 * UI state for the calculator inputs.
 * Matches the behaviour and fields of the iOS CalculatorView.swift.
 */
data class CalculatorInputState(
    val targetSizeText: String = "",
    val selectedMaterial: String = "Aluminium 2014",
    val selectedTemp: Int = 20
)