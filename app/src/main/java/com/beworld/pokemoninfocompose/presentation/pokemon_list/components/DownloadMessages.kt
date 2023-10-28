package com.beworld.pokemoninfocompose.presentation.pokemon_list.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.beworld.pokemoninfocompose.R


@Composable
fun DownloadResultMessage(isCorrect: Boolean) {
    AlertDialog(
        title = { Text(text = if (isCorrect) "Download Success" else "Error") },
        onDismissRequest = { /*TODO*/ },
        confirmButton = { /*TODO*/ }
    )
}


@Composable
fun DownloadMessage() {
    AlertDialog(
        title = {
            Row {
                Text(text = "Downloading")
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.medium_padding))
                )
            }
        },
        onDismissRequest = { /*TODO*/ },
        confirmButton = { /*TODO*/ }
    )
}