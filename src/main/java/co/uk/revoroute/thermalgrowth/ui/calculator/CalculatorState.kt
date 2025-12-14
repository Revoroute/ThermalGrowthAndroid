package co.uk.revoroute.thermalgrowth.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.revoroute.thermalgrowth.app.AppSettingsStore
import co.uk.revoroute.thermalgrowth.data.MaterialRepository
import co.uk.revoroute.thermalgrowth.model.CalculationEngine
import co.uk.revoroute.thermalgrowth.model.CalculationResult
import co.uk.revoroute.thermalgrowth.model.Material
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CalculatorState(
    private val repository: MaterialRepository,
    private val settings: AppSettingsStore
) : ViewModel() {

    // Text field state
    private val _measuredSize = MutableStateFlow("")
    val measuredSize: StateFlow<String> = _measuredSize

    private val _measuredTemp = MutableStateFlow("")
    val measuredTemp: StateFlow<String> = _measuredTemp

    // Selected material
    private val _selectedMaterial = MutableStateFlow<Material?>(null)
    val selectedMaterial: StateFlow<Material?> = _selectedMaterial

    // Loaded materials list
    private val _materials = MutableStateFlow<List<Material>>(emptyList())
    val materials: StateFlow<List<Material>> = _materials

    // Calculation result
    private val _calculationResult = MutableStateFlow<CalculationResult?>(null)
    val calculationResult: StateFlow<CalculationResult?> = _calculationResult

    init {
        viewModelScope.launch {
            val loaded = repository.loadMaterials()
            _materials.value = loaded
            if (loaded.isNotEmpty()) {
                _selectedMaterial.value = loaded.first()
                updateCalculation()
            }
        }

        viewModelScope.launch {
            settings.unitSystem.collect {
                updateCalculation()
            }
        }

        viewModelScope.launch {
            settings.referenceTempC.collect {
                updateCalculation()
            }
        }
    }

    fun onMeasuredSizeChanged(value: String) {
        _measuredSize.value = value
        updateCalculation()
    }

    fun onMeasuredTempChanged(value: String) {
        _measuredTemp.value = value
        updateCalculation()
    }

    fun onMaterialSelected(material: Material) {
        _selectedMaterial.value = material
        updateCalculation()
    }

    private fun updateCalculation() {
        val size = _measuredSize.value.toDoubleOrNull()
        val tempInput = _measuredTemp.value.toDoubleOrNull()
        val material = _selectedMaterial.value

        if (size == null || tempInput == null || material == null) {
            _calculationResult.value = null
            return
        }

        // Convert measured temperature to °C using explicit Double-safe logic (no rounding)
        val measuredTempC = tempInput

        // Convert measured length to mm using settings
        val measuredSizeMm =
            if (settings.unitSystem.value == AppSettingsStore.UnitSystem.IMPERIAL) {
                size * 25.4
            } else {
                size
            }
        // Reference temperature is always stored internally as °C (Double-safe)
        val referenceTempC = settings.referenceTempC.value.toDouble()

        _calculationResult.value = CalculationEngine.calculateCorrectedSize(
            measuredSize = measuredSizeMm,
            materialAlpha = material.alpha,
            measuredTemp = measuredTempC,
            referenceTemp = referenceTempC
        )

        settings.incrementCalculationCount()
    }
}