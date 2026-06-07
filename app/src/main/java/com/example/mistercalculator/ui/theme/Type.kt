package com.example.mistercalculator.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mistercalculator.R

// Set of Material typography styles to start with
val Spacer = FontFamily(
    Font(R.font.spacerotesk_regular)
)

val Geist = FontFamily(
    Font(R.font.geist_regular)
)

val JetBrains = FontFamily(
    Font(R.font.jetbrainsmono_regular)
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Spacer,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Spacer,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Spacer,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Geist,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    labelMedium = TextStyle(
        fontFamily = JetBrains,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    labelSmall = TextStyle(
        fontFamily = JetBrains,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)