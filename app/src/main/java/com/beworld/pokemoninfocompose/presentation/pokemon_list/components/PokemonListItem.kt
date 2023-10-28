package com.beworld.pokemoninfocompose.presentation.pokemon_list.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiBad
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.beworld.pokemoninfocompose.R
import com.beworld.task1.domain.model.Pokemon
import java.time.chrono.IsoEra

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonListItem(
    pokemon: Pokemon,
    imageBitmap: ImageBitmap?,
    isError: Boolean,
    onItemClick: () -> Unit,
    onLongItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = { isMenuExpanded = true }
            )
    ) {
        Box(
            Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.medium_padding))
                .size(96.dp)
        ) {
            if (isError) {
                Image(
                    imageVector = Icons.Filled.SignalWifiBad,
                    contentDescription = "Bad Internet connection",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            } else if (imageBitmap == null) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Pokemon ${pokemon.name}",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Text(
            text = pokemon.name,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        if (pokemon.fromRemote) {
            Menu(
                expanded = isMenuExpanded,
                onDismiss = { isMenuExpanded = false },
                download = onLongItemClick
            )
        }
    }
}