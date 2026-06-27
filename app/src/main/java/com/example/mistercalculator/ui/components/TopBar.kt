package com.example.mistercalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mistercalculator.navigation.Routes
import com.example.mistercalculator.ui.theme.JetBrains

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorTopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(62.dp)
            .background(
                color = MaterialTheme.colorScheme.background
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "",
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = JetBrains,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,

                shadow = Shadow(
                    color = MaterialTheme.colorScheme.primary,
                    offset = Offset(x = 0f, y = 2f),
                    blurRadius = 56f
                )
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PopupMenu(
                onAboutClick = {},
                onSettingClick = {
                    navController.navigate(
                        Routes.SETTING_SCREEN
                    )
                },
                onHelpClick = {},
            )
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "History",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        navController.navigate(
                            Routes.HISTORY_SCREEN
                        )
                    }
            )
        }
    }
}