package com.example.mistercalculator.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mistercalculator.viewmodel.CalculatorViewModel

@Composable
fun DisplayScreen(
    expression: String,
    result: String,
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel
) {
    val scrollState = rememberScrollState()
    val expressionSize by animateFloatAsState(
        targetValue =
            if (viewModel.isResultMode) 0f
            else 38f,
        label = "expressionSize"
    )
    val resultSize by animateFloatAsState(
        targetValue =
            if (viewModel.isResultMode) 56f
            else 24f,
        label = "resultSize"
    )
    val expressionAlpha by animateFloatAsState(
        targetValue =
            if (viewModel.isResultMode) 0f
            else 1f,
        label = "expressionAlpha"
    )
    val resultTopPadding by animateDpAsState(
        targetValue =
            if (viewModel.isResultMode) 80.dp
            else 0.dp,
        label = "resultPadding"
    )

    Text(
        text = expression,
        fontSize = expressionSize.sp,
        softWrap = true,
        modifier = Modifier
            .horizontalScroll(scrollState)
            .alpha(expressionAlpha)
    )
    Text(
        text = result,
        fontSize = resultSize.sp,
        modifier = Modifier.padding(
            top = resultTopPadding
        )
    )
}