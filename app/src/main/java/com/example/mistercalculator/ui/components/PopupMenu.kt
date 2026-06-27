package com.example.mistercalculator.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun PopupMenu(
    onAboutClick: () -> Unit,
    onSettingClick: () -> Unit,
    onHelpClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        }
    ) {
        DropdownMenuItem(
            text = { Text("About Us") },
            onClick = {
                onAboutClick()
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text("Settings") },
            onClick = {
                onSettingClick()
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text("Help") },
            onClick = {
                onHelpClick()
                expanded = false
            }
        )
    }
}