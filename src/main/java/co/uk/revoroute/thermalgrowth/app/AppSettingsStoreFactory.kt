package co.uk.revoroute.thermalgrowth.app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppSettingsStoreFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppSettingsStore::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppSettingsStore(
                context.applicationContext
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}