package com.hossein.dev.iso8583lab.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "stan_pref")

class StanDataStore(
    private val context: Context
) {
    companion object {
        private val KEY_STAN = intPreferencesKey("stan")

        private const val MAX_STAN = 999_999
        private const val MIN_STAN = 1
    }

    val stanFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_STAN] ?: MIN_STAN
    }

    suspend fun saveStan(value: Int) {
        context.dataStore.edit { prefs ->
            val newValue = if (value > MAX_STAN) MIN_STAN else value
            prefs[KEY_STAN] = newValue
        }
    }
}