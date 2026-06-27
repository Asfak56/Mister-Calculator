package com.example.mistercalculator.ui.components

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
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
    val expressionSize = if (viewModel.isResultMode)
        28.sp
    else
        48.sp

    val resultSize = if (viewModel.isResultMode)
        56.sp
    else
        24.sp

    val scrollState = rememberScrollState()
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

    val expressionFontSize = when {
        expression.length < 10 -> 48.sp
        expression.length < 20 -> 40.sp
        expression.length < 30 -> 32.sp
        else -> 24.sp
    }
    val resultFontSize = when {
        result.length < 10 -> 56.sp
        result.length < 15 -> 48.sp
        result.length < 20 -> 40.sp
        result.length < 25 -> 34.sp
        else -> 28.sp
    }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Text(
        text = expression,
        fontSize = expressionSize,
        softWrap = true,
        modifier = Modifier
            .horizontalScroll(scrollState)
            .alpha(expressionAlpha)
    )
    Text(
        text = result,
        fontSize = resultSize,
        modifier = Modifier
            .padding(top = resultTopPadding)
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    clipboardManager.setText(
                        AnnotatedString(result)
                    )

                    Toast.makeText(
                        context,
                        "Result copied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
    )
}