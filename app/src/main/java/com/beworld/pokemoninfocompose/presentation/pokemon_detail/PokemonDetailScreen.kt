package com.beworld.pokemoninfocompose.presentation.pokemon_detail

import android.annotation.SuppressLint
import android.graphics.Paint
import android.icu.text.CaseMap.Title
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiBad
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.beworld.pokemoninfocompose.R
import com.beworld.pokemoninfocompose.presentation.application.ApplicationScreens
import com.beworld.pokemoninfocompose.presentation.pokemon_detail.components.DetailMenu
import com.beworld.pokemoninfocompose.presentation.pokemon_list.components.DownloadMessage
import com.beworld.pokemoninfocompose.presentation.pokemon_list.components.DownloadResultMessage
import com.beworld.task1.common.Constants
import com.beworld.task1.pokemosso.Pokemosso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val state = viewModel.pokemonDetailStateHolder.value
    val pokemon = state.pokemon
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(id = R.dimen.medium_padding))
    ) {
        var pokemonBitmap by remember {
            mutableStateOf<ImageBitmap?>(null)
        }
        var isError by remember { mutableStateOf(false) }

        var isMenuExpanded by remember {
            mutableStateOf(false)
        }
        val isCorrect =
            remember { mutableStateOf<Boolean?>(null) }
        var isLoading by remember {
            mutableStateOf(false)
        }

        pokemon?.let {
            Pokemosso.get()
                .load(it.photoUrl)
                .resize(248, 248)
                .get(object : Pokemosso.Callback {
                    override fun onComplete(bitmap: ImageBitmap?) {
                        pokemonBitmap = bitmap
                    }

                    override fun onError() {
                        isError = true
                    }
                })
        }
        Box(
            Modifier
                .height(300.dp)
                .align(CenterHorizontally)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {},
                    onLongClick = { isMenuExpanded = true }
                )
        ) {
            if (isError) {
                Image(
                    imageVector = Icons.Filled.SignalWifiBad,
                    contentDescription = "Bad Internet connection",
                    modifier = Modifier
                        .align(Center)
                        .size(48.dp)
                        .fillMaxSize()
                )
            } else if (pokemonBitmap == null) {
                CircularProgressIndicator(Modifier.align(Center))
            } else {
                Image(
                    bitmap = pokemonBitmap!!,
                    contentDescription = "Pokemon ${pokemon?.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Center)
                        .fillMaxSize()
                )

                if (isMenuExpanded) {
                    pokemon?.photoUrl?.let {
                        DetailMenu(
                            expanded = isMenuExpanded,
                            onDismiss = { isMenuExpanded = false },
                            url = it,
                            isCorrect = isCorrect,
                            setLoading = { loading -> isLoading = loading }
                        )
                    }
                }
            }
        }

        if (isLoading) {
            DownloadMessage()
        }

        isCorrect.value?.let {
            isLoading = false
            DownloadResultMessage(isCorrect = it)
            rememberCoroutineScope().launch {
                delay(1000)
                isCorrect.value = null
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.medium_padding))
        ) {
            Text(text = "Name: ${pokemon?.name}")
            Text(text = "Experience: ${pokemon?.experience}")
            Text(text = "Weight: ${pokemon?.weight}")
            Text(text = "Height: ${pokemon?.height}")
        }
    }
}