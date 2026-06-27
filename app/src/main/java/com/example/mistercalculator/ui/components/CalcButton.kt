package com.example.mistercalculator.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mistercalculator.data.CalculatorButtonsData

@Composable
fun Buttons(button: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .height(56.dp)
                .width(72.dp),
            contentColor = contentColor(button),
            containerColor = if (button == "=") {
                Color.Cyan
            } else {
                MaterialTheme.colorScheme.surface
            }
        ) {
            Text(
                text = button,
                fontSize = 22.sp,
            )
        }
    }
}

@Composable
fun contentColor(btn: String): Color {
    if (btn == "AC")
        return Color.Red

    if (btn in CalculatorButtonsData.cyanButtons)
        return MaterialTheme.colorScheme.primary

    if (btn == "=")
        return Color.Black

    return MaterialTheme.colorScheme.onSurface
}

