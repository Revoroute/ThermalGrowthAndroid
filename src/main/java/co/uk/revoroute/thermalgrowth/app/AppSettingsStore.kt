package co.uk.revoroute.thermalgrowth.app

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

// DataStore instance
private val Context.dataStore by preferencesDataStore(name = "app_settings")

class AppSettingsStore(
    private val context: Context
) : ViewModel() {

    // -------------------------
    // Enums (iOS parity)
    // -------------------------

    enum class UnitSystem {
        METRIC,      // mm / °C
        IMPERIAL     // in / °F
    }

    enum class AppTheme {
        SYSTEM,
        LIGHT,
        DARK,
        HIGH_CONTRAST
    }

    enum class AppAccent {
        BLUE,
        ORANGE,
        GREEN,
        RED,
        PURPLE
    }

    // -------------------------
    // Preference keys
    // -------------------------

    private object Keys {
        val UNIT_SYSTEM = stringPreferencesKey("unit_system")
        val THEME = stringPreferencesKey("theme")
        val ACCENT = stringPreferencesKey("accent")
        val REFERENCE_TEMP_C = intPreferencesKey("reference_temp_c")
        val CALCULATION_COUNT = intPreferencesKey("calculation_count")
    }

    // -------------------------
    // Internal state (canonical)
    // -------------------------

    private val _unitSystem = MutableStateFlow(UnitSystem.METRIC)
    val unitSystem: StateFlow<UnitSystem> = _unitSystem.asStateFlow()

    private val _theme = MutableStateFlow(AppTheme.SYSTEM)
    val theme: StateFlow<AppTheme> = _theme.asStateFlow()

    private val _accent = MutableStateFlow(AppAccent.BLUE)
    val accent: StateFlow<AppAccent> = _accent.asStateFlow()

    // Reference temperature is ALWAYS stored in °C
    private val _referenceTempC = MutableStateFlow(20)
    val referenceTempC: StateFlow<Int> = _referenceTempC.asStateFlow()

    private val _calculationCount = MutableStateFlow(0)
    val calculationCount: StateFlow<Int> = _calculationCount.asStateFlow()

    // -------------------------
    // Init: load from DataStore
    // -------------------------

    init {
        viewModelScope.launch {
            context.dataStore.data.collect { prefs ->

                _unitSystem.value =
                    prefs[Keys.UNIT_SYSTEM]
                        ?.let { runCatching { UnitSystem.valueOf(it) }.getOrNull() }
                        ?: UnitSystem.METRIC

                _theme.value =
                    prefs[Keys.THEME]
                        ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
                        ?: AppTheme.SYSTEM

                _accent.value =
                    prefs[Keys.ACCENT]
                        ?.let { runCatching { AppAccent.valueOf(it) }.getOrNull() }
                        ?: AppAccent.BLUE

                _referenceTempC.value =
                    prefs[Keys.REFERENCE_TEMP_C] ?: 20

                _calculationCount.value =
                    prefs[Keys.CALCULATION_COUNT] ?: 0
            }
        }
    }

    // -------------------------
    // Mutators (single write path)
    // -------------------------

    fun setUnitSystem(system: UnitSystem) {
        _unitSystem.value = system
        saveString(Keys.UNIT_SYSTEM, system.name)
    }

    fun setTheme(theme: AppTheme) {
        _theme.value = theme
        saveString(Keys.THEME, theme.name)
    }

    fun setAccent(accent: AppAccent) {
        _accent.value = accent
        saveString(Keys.ACCENT, accent.name)
    }

    fun setReferenceTempC(tempC: Int) {
        _referenceTempC.value = tempC
        saveInt(Keys.REFERENCE_TEMP_C, tempC)
    }

    fun incrementCalculationCount() {
        val newCount = _calculationCount.value + 1
        _calculationCount.value = newCount
        saveInt(Keys.CALCULATION_COUNT, newCount)
    }

    // -------------------------
    // Conversions (canonical)
    // -------------------------

    fun toCelsius(input: Int): Int =
        if (_unitSystem.value == UnitSystem.METRIC) input
        else ((input - 32) * 5.0 / 9.0).toInt()

    fun toCelsius(input: Double): Double =
        if (_unitSystem.value == UnitSystem.METRIC) input
        else (input - 32.0) * 5.0 / 9.0

    fun mmToInches(mm: Double): Double =
        mm / 25.4

    fun inchesToMm(inches: Double): Double =
        inches * 25.4

    // -------------------------
    // Display helpers (UI ONLY)
    // -------------------------

    fun displayTemperature(tempC: Int): String =
        if (_unitSystem.value == UnitSystem.METRIC) {
            "$tempC°C"
        } else {
            val f = (tempC * 9.0 / 5.0) + 32.0
            "${f.toInt()}°F"
        }

    fun displayReferenceTemperature(): String =
        displayTemperature(_referenceTempC.value)

    fun displayLength(mm: Double): String =
        if (_unitSystem.value == UnitSystem.METRIC) {
            String.format(Locale.US, "%.3f mm", mm)
        } else {
            String.format(Locale.US, "%.4f in", mmToInches(mm))
        }

    fun displayInputSizeHint(): String =
        if (_unitSystem.value == UnitSystem.METRIC)
            "Enter Size (mm)"
        else
            "Enter Size (in)"

    fun displayTemperatureLabel(): String =
        if (_unitSystem.value == UnitSystem.METRIC)
            "Temperature (°C)"
        else
            "Temperature (°F)"

    // -------------------------
    // Persistence helpers
    // -------------------------

    private fun saveString(key: Preferences.Key<String>, value: String) {
        viewModelScope.launch {
            context.dataStore.edit { it[key] = value }
        }
    }

    private fun saveInt(key: Preferences.Key<Int>, value: Int) {
        viewModelScope.launch {
            context.dataStore.edit { it[key] = value }
        }
    }
}