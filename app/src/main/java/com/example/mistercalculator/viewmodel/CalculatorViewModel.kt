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
import java.math.BigInteger
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

    var showLiveResult by mutableStateOf(false)
        private set

    private var isResultShown = false

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

    private var historyAddForCurrentResult = false

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

            if (
                it.isEmpty() &&
                btn in listOf(
                    "+",
                    "x",
                    "\u00F7",
                    "%",
                    "."
                )
            ) {
                return
            }

            if (btn == "AC") {
                _expression.value = ""
                _result.value = ""

                isResultShown = false
                isResultMode = false

                saveCalculatorState()
                return
            }

            if (isResultShown) {
                if (btn == "=" || btn == "\u232B") {
                    return
                }
                if (btn in operators) {
                    _expression.value = (_result.value ?: "") + btn
                } else {
                    _expression.value = btn
                    _result.value = ""
                }

                isResultShown = false
                isResultMode = false

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
                if (it.length == 1 && it == "-") {
                    return
                }

                _expression.value = it.dropLast(1) + btn

                return
            }

            // Auto 0.
            if (btn == "." && (it.isEmpty() || lastChar in operators)) {
                _expression.value = it + "0."
                calculateLiveResult()
                saveCalculatorState()
                return
            }

            if (btn == ".") {
                val currentNumber = it.split(
                    "+",
                    "-",
                    "x",
                    "\u00F7",
                    "%"
                ).last()

                if (currentNumber.contains(".")) {
                    return
                }
            }

            historyAddForCurrentResult = false

            if (
                btn.firstOrNull()?.isDigit() == true && it.length >= 30
            ) {
                return
            }

            _expression.value = it + btn
            calculateLiveResult()
            saveCalculatorState()
        }
    }

    fun formatExpression(expression: String): String {
        val regex = Regex("\\d+(\\.\\d+)?")

        return regex.replace(expression) { match ->
            val number = match.value

            if (number.contains(".")) {
                val parts = number.split(".")
                val integerPart = DecimalFormat("#,###")
                    .format(BigInteger(parts[0]))

                integerPart + "." + parts[1]
            } else {
                DecimalFormat("#,###")
                    .format(BigInteger(number))
            }
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

            isResultShown = true
            saveCalculatorState()

            if (!historyAddForCurrentResult) {
                historyAddForCurrentResult = true
            }

            addHistory()
        } catch (e: Exception) {
            _result.value = "Error"
            saveCalculatorState()
        }
    }

    // Live result
    private fun calculateLiveResult() {
        val operators = listOf(
            "+",
            "-",
            "x",
            "\u00F7",
            "%",
            "."
        )

        val expression = _expression.value ?: ""

        if (expression.isBlank()) {
            _result.value = ""
            return
        }

        showLiveResult = false

        val hasOperator = operators.any { expression.contains(it) }

        if (!hasOperator) {
            _result.value = ""
            return
        }

        val lastChar = expression.lastOrNull()?.toString()

        if (lastChar in operators) {
            _result.value = ""
            return
        }

        showLiveResult = true

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