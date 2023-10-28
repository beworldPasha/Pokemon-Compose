package com.beworld.pokemoninfocompose.presentation.pokemon_list.components

import android.widget.PopupMenu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Menu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismiss: () -> Unit,
    download: () -> Unit
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text(text = "Download with Image") },
            onClick = {
                onDismiss()
            }
        )
        DropdownMenuItem(
            text = { Text(text = "Download") },
            onClick = {
                onDismiss()
                download()
            }
        )
    }
}