package com.example.mistercalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mistercalculator.data.HistoryRepository
import com.example.mistercalculator.data.preferences.ThemePreferences

class CalculatorViewModelFactory(
    private val repository: HistoryRepository,
    private val themePreferences: ThemePreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (
            modelClass.isAssignableFrom(
                CalculatorViewModel::class.java
            )
        ) {
            return CalculatorViewModel(
                repository,
                themePreferences = themePreferences
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}