package com.example.mistercalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mistercalculator.ui.theme.Spacer

@Composable
fun CalculatorBottomBar(
    onCalculatorClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    var selected by remember { mutableStateOf("Calculator") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(78.dp)
            .background(
                color = MaterialTheme.colorScheme.surface
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 36.dp, end = 36.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = "Calculator",
                    tint = if (selected == "Calculator") {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Gray
                    },
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            selected = "Calculator"
                            onCalculatorClick()
                        }
                )
                Text(
                    text = "Calculator",
                    color = if (selected == "Calculator") {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Gray
                    },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Spacer,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CurrencyExchange,
                    contentDescription = "Unit Converter",
                    tint = if (selected == "Converter") {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Gray
                    },
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            selected = "Converter"
                            onSettingClick()
                        }
                )
                Text(
                    text = "Converter",
                    color = if (selected == "Converter") {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Gray
                    },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Spacer,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}