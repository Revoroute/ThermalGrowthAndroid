package co.uk.revoroute.thermalgrowth.ui.settings

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// DataStore instance
private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsViewModel(private val context: Context) : ViewModel() {

    // Preference keys (matches AppSettings.swift)
    companion object {
        private val ACCENT_CHOICE = stringPreferencesKey("accentChoice")
        private val UNIT_PREF = stringPreferencesKey("unitPreference")
        private val COLOR_THEME = stringPreferencesKey("colorTheme")
        private val REFERENCE_TEMP = intPreferencesKey("referenceTemperature")
        private val CALC_COUNT = intPreferencesKey("calculationCount")
    }

    // Enums mirroring iOS AppSettings
    enum class AppAccent { BLUE, ORANGE, GREEN, RED, PURPLE }
    enum class UnitPreference { CELSIUS, FAHRENHEIT }
    enum class AppTheme { SYSTEM, LIGHT, DARK, HIGH_CONTRAST }

    // Exposed StateFlows
    private val _accent = MutableStateFlow(AppAccent.BLUE)
    val accent: StateFlow<AppAccent> = _accent

    private val _units = MutableStateFlow(UnitPreference.CELSIUS)
    val units: StateFlow<UnitPreference> = _units

    private val _theme = MutableStateFlow(AppTheme.SYSTEM)
    val theme: StateFlow<AppTheme> = _theme

    private val _referenceTemp = MutableStateFlow(20)
    val referenceTemp: StateFlow<Int> = _referenceTemp

    private val _calculationCount = MutableStateFlow(0)
    val calculationCount: StateFlow<Int> = _calculationCount

    init {
        // Load persisted settings
        viewModelScope.launch {
            context.dataStore.data.collect { prefs ->

                _accent.value =
                    AppAccent.valueOf(prefs[ACCENT_CHOICE] ?: AppAccent.BLUE.name)

                _units.value =
                    UnitPreference.valueOf(prefs[UNIT_PREF] ?: UnitPreference.CELSIUS.name)

                _theme.value =
                    AppTheme.valueOf(prefs[COLOR_THEME] ?: AppTheme.SYSTEM.name)

                _referenceTemp.value = prefs[REFERENCE_TEMP] ?: 20

                _calculationCount.value = prefs[CALC_COUNT] ?: 0
            }
        }
    }

    // Save functions
    fun setAccent(newAccent: AppAccent) {
        _accent.value = newAccent
        saveString(ACCENT_CHOICE, newAccent.name)
    }

    fun setUnits(newUnits: UnitPreference) {
        _units.value = newUnits
        saveString(UNIT_PREF, newUnits.name)
    }

    fun setTheme(newTheme: AppTheme) {
        _theme.value = newTheme
        saveString(COLOR_THEME, newTheme.name)
    }

    fun setReferenceTemp(temp: Int) {
        _referenceTemp.value = temp
        saveInt(REFERENCE_TEMP, temp)
    }

    fun incrementCalculationCount() {
        val newCount = _calculationCount.value + 1
        _calculationCount.value = newCount
        saveInt(CALC_COUNT, newCount)
    }

    // DataStore persistence
    private fun saveString(key: Preferences.Key<String>, value: String) {
        viewModelScope.launch {
            context.dataStore.edit { prefs -> prefs[key] = value }
        }
    }

    private fun saveInt(key: Preferences.Key<Int>, value: Int) {
        viewModelScope.launch {
            context.dataStore.edit { prefs -> prefs[key] = value }
        }
    }
}