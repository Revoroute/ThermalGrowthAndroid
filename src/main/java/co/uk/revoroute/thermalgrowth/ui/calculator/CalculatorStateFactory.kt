package co.uk.revoroute.thermalgrowth.ui.calculator

import co.uk.revoroute.thermalgrowth.app.AppSettingsStore

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.uk.revoroute.thermalgrowth.data.MaterialRepository

class CalculatorStateFactory(
    private val context: Context,
    private val settings: AppSettingsStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorState::class.java)) {

            val repository = MaterialRepository(context.applicationContext)

            @Suppress("UNCHECKED_CAST")
            return CalculatorState(
                repository = repository,
                settings = settings
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}