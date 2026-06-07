package com.example.mistercalculator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.mistercalculator.data.CalculatorButtonsData
import com.example.mistercalculator.ui.components.Buttons
import com.example.mistercalculator.ui.components.DisplayScreen
import com.example.mistercalculator.viewmodel.CalculatorViewModel

@Composable
fun HomeScreen(viewModel: CalculatorViewModel) {
    val expression = viewModel.expression.observeAsState()
    val result = viewModel.result.observeAsState()
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        DisplayScreen(
            expression = viewModel.formatExpression(expression.value ?: ""),
            result = result.value ?: "",
            viewModel = viewModel
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4)
        ) {
            items(CalculatorButtonsData.buttons) {
                Buttons(
                    button = it,
                    onClick = {
                        haptic.performHapticFeedback(
                            HapticFeedbackType.Confirm
                        )
                        viewModel.onButtonClick(it)
                    }
                )
            }
        }
    }
}