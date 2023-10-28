package com.beworld.pokemoninfocompose.presentation.camera.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RemoveDialog(
    isVisible: Boolean,
    dismiss: () -> Unit,
    confirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = { Text(text = "Remove Photo?") },
        onDismissRequest = dismiss,
        confirmButton = {
            Button(onClick = confirm) {
                Text("Remove")
            }
        },
        dismissButton = {
            Button(onClick = dismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier
    )
}

@Composable
fun RemoveMenu(
    onDismiss: () -> Unit,
    dialogVisible: MutableState<Boolean>,
    convert: () -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text(text = "Remove") },
            onClick = {
                onDismiss()
                dialogVisible.value = true
            }
        )
        DropdownMenuItem(
            text = { Text(text = "Convert to Black And White") },
            onClick = {
                convert()
                onDismiss()
            }
        )
    }
}