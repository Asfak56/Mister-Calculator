package com.example.mistercalculator.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mistercalculator.data.CalculatorStateEntity
import com.example.mistercalculator.data.HistoryEntity
import com.example.mistercalculator.data.HistoryRepository
import com.example.mistercalculator.data.preferences.ThemePreferences
import com.example.mistercalculator.ui.theme.Theme
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat

class CalculatorViewModel(
    private val repository: HistoryRepository,
    private val themePreferences: ThemePreferences
) : ViewModel() {
    // Expression
    private val _expression = MutableLiveData("")
    val expression: LiveData<String> = _expression

    // Result
    private val _result = MutableLiveData("")
    val result: LiveData<String> = _result

    // Result Expanding
    var isResultMode by mutableStateOf(false)
        private set

    var historyItem by mutableStateOf<List<HistoryEntity>>(
        emptyList()
    )
        private set

    var currentTheme by mutableStateOf<Theme?>(
        null
    )
        private set

    init {
        viewModelScope.launch {
            repository
                .getAllHistory()
                .collect {
                    historyItem = it
                }
        }

        viewModelScope.launch {
            val savedState = repository.getCalculatorState()

            savedState?.let {
                _expression.value = it.expression

                _result.value = it.result
            }
        }

        viewModelScope.launch {
            themePreferences.themeFlow.collect { theme ->
                currentTheme = if (theme == "DARK") {
                    Theme.DARK
                } else {
                    Theme.LIGHT
                }
            }
        }
    }

    // Set Theme
    fun setTheme(
        theme: Theme
    ) {
        currentTheme = theme

        viewModelScope.launch {
            themePreferences.saveTheme(
                theme.name
            )
        }
    }

    // Save function
    private fun saveCalculatorState() {
        viewModelScope.launch {
            repository.saveCalculatorState(
                CalculatorStateEntity(
                    expression = _expression.value ?: "",
                    result = _result.value ?: ""
                )
            )
        }
    }

    // Add History
    fun addHistory() {
        _expression.value?.let {
            if (it.isBlank()) return

            if (result.value == "Error") return

            viewModelScope.launch {
                repository.insertHistory(
                    HistoryEntity(
                        expression = _expression.value ?: "",
                        result = _result.value ?: ""
                    )
                )
            }
        }
    }

    // Clear All History
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // On Button Click
    fun onButtonClick(btn: String) {
        _expression.value?.let {
            val operators = listOf(
                "+",
                "-",
                "x",
                "\u00F7",
                "%"
            )

            if (btn == "AC") {
                _expression.value = ""
                _result.value = ""

                saveCalculatorState()
                return
            }

            if (btn == "\u232B") {
                if (it.isEmpty()) {
                    return
                }
                _expression.value =
                    it.dropLast(1)
                calculateLiveResult()
                saveCalculatorState()
                return
            }

            if (btn == "=") {
                if (it.isEmpty()) {
                    _result.value = ""
                    return
                }
                calculateResult()

                isResultMode = true

                return
            }

            // Prevent double operator
            val lastChar = it.lastOrNull()?.toString()

            if (btn in operators && lastChar in operators) {
                return
            }

            // Auto 0.
            if (btn == "." && (it.isEmpty() || lastChar in operators)) {
                _expression.value = it + "0."
                calculateLiveResult()
                saveCalculatorState()
                return
            }

            isResultMode = false

            _expression.value = it + btn
            calculateLiveResult()
            saveCalculatorState()
        }
    }

    fun formatExpression(expression: String): String {
        val regex = Regex("\\d+")

        return regex.replace(expression) {
            val number = it.value.toLong()

            DecimalFormat("#,###")
                .format(number)
        }
    }

    // Calculate Results
    private fun calculateResult() {
        try {
            val expression = _expression.value ?: ""
            val cleanedExpression = expression
                .replace("x", "*")
                .replace("\u00F7", "/")
            val result = ExpressionBuilder(
                cleanedExpression
            )
                .build()
                .evaluate()

            val formatter = DecimalFormat("#,###.######")

            _result.value = if (result % 1 == 0.0) {
                formatter.format(
                    result.toInt(),
                )
            } else {
                formatter.format(result)
            }

            saveCalculatorState()
            addHistory()
        } catch (e: Exception) {
            _result.value = "Error"
            saveCalculatorState()
        }
    }

    // Live result
    private fun calculateLiveResult() {
        val expression = _expression.value ?: ""

        if (expression.isBlank()) {
            _result.value = ""
            return
        }

        val operators = listOf(
            "+",
            "-",
            "x",
            "\u00F7",
            "%",
            "."
        )
        val lastChar = expression.lastOrNull()?.toString()

        if (lastChar in operators) {
            return
        }

        try {
            val cleanedExpression = expression
                .replace("x", "*")
                .replace("\u00F7", "/")
            val result = ExpressionBuilder(
                cleanedExpression
            )
                .build()
                .evaluate()
            val formatter = DecimalFormat("#,###.######")
            _result.value = if (result % 1 == 0.0) {
                formatter.format(
                    result.toInt()
                )
            } else {
                formatter.format(
                    result
                )
            }
        } catch (e: Exception) {
        }
    }

    // Restore Function
    fun restoreCalculation(
        expression: String,
        result: String
    ) {
        _expression.value = expression
        _result.value = result

        saveCalculatorState()
    }

    fun processVoiceInput(
        spokenText: String
    ) {
        val voiceText = spokenText.lowercase()

        val parsedExpression = voiceText
            .replace("zero", "0")
            .replace("one", "1")
            .replace("two", "2")
            .replace("three", "3")
            .replace("four", "4")
            .replace("five", "5")
            .replace("six", "6")
            .replace("seven", "7")
            .replace("eight", "8")
            .replace("nine", "9")

            .replace("plus", "+")
            .replace("minus", "-")
            .replace("into", "x")
            .replace("times", "x")
            .replace("multiplied by", "x")
            .replace("divide by", "\u00F7")
            .replace("divided by", "\u00F7")
            .replace("point", ".")
            .replace("dot", ".")
            .replace("mode by", "%")

            .replace(" ", " ")

        isResultMode = false

        _expression.value = parsedExpression

        calculateLiveResult()
        saveCalculatorState()
        calculateResult()
    }
}