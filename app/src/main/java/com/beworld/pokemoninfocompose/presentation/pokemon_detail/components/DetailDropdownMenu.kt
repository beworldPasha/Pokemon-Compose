package com.beworld.pokemoninfocompose.presentation.pokemon_detail.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import com.beworld.pokemoninfocompose.presentation.pokemon_list.components.DownloadResultMessage
import com.beworld.task1.pokemosso.Pokemosso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismiss: () -> Unit,
    setLoading: (Boolean) -> Unit,
    isCorrect: MutableState<Boolean?>,
    url: String
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text(text = "Download Image") },
            onClick = {
                Pokemosso.get()
                    .load(url)
                    .intoDevice(callback = object : Pokemosso.Callback {
                        override fun onComplete(bitmap: ImageBitmap?) {
                            isCorrect.value = true
                        }

                        override fun onError() {
                            isCorrect.value = false
                        }

                        override fun onLoading() {
                            setLoading(true)
                        }
                    })
                onDismiss()
            }
        )
    }
}