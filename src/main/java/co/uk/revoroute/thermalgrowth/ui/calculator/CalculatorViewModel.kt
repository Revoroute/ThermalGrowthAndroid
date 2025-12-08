package co.uk.revoroute.thermalgrowth.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.revoroute.thermalgrowth.data.MaterialRepository
import co.uk.revoroute.thermalgrowth.model.CalculationEngine
import co.uk.revoroute.thermalgrowth.model.CalculationResult
import co.uk.revoroute.thermalgrowth.model.Material
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val repository: MaterialRepository
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

    // Result state
    private val _calculationResult = MutableStateFlow<CalculationResult?>(null)
    val calculationResult: StateFlow<CalculationResult?> = _calculationResult

    // Sheet visibility
    private val _showResultSheet = MutableStateFlow(false)
    val showResultSheet: StateFlow<Boolean> = _showResultSheet

    // For rating trigger later
    private val _calculationCount = MutableStateFlow(0)
    val calculationCount: StateFlow<Int> = _calculationCount

    init {
        // Load materials on startup
        viewModelScope.launch {
            val loaded = repository.loadMaterials()
            _materials.value = loaded
            if (loaded.isNotEmpty()) {
                _selectedMaterial.value = loaded.first()
            }
        }
    }

    // User input handlers
    fun onMeasuredSizeChanged(value: String) {
        _measuredSize.value = value
    }

    fun onMeasuredTempChanged(value: String) {
        _measuredTemp.value = value
    }

    fun onMaterialSelected(material: Material) {
        _selectedMaterial.value = material
    }

    // Main calculation
    fun calculate() {
        val size = measuredSize.value.toDoubleOrNull()
        val temp = measuredTemp.value.toDoubleOrNull()
        val material = selectedMaterial.value

        if (size != null && temp != null && material != null) {
            val result = CalculationEngine.calculateCorrectedSize(
                measuredSize = size,
                materialAlpha = material.alpha,
                measuredTemp = temp
            )

            _calculationResult.value = result
            _showResultSheet.value = true

            // Increase count for rating logic
            _calculationCount.value += 1
        }
    }

    fun dismissResultSheet() {
        _showResultSheet.value = false
    }

    // Enable button when valid
    fun isCalculateEnabled(): Boolean {
        return measuredSize.value.toDoubleOrNull() != null &&
                measuredTemp.value.toDoubleOrNull() != null &&
                selectedMaterial.value != null
    }
}