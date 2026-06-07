package com.example.mistercalculator.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(
    name = "settings"
)

private val THEME_KEY = stringPreferencesKey("theme")

class ThemePreferences(
    private val context: Context
) {
    suspend fun saveTheme(
        theme: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }

    val themeFlow = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: "LIGHT"
        }
}