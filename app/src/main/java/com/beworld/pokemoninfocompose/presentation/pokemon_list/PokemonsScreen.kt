package com.beworld.pokemoninfocompose.presentation.pokemon_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.beworld.pokemoninfocompose.R
import com.beworld.pokemoninfocompose.presentation.application.ApplicationScreens
import com.beworld.pokemoninfocompose.presentation.application.MenuItems
import com.beworld.pokemoninfocompose.presentation.pokemon_list.components.DownloadMessage
import com.beworld.pokemoninfocompose.presentation.pokemon_list.components.DownloadResultMessage
import com.beworld.pokemoninfocompose.presentation.pokemon_list.components.Menu
import com.beworld.pokemoninfocompose.presentation.pokemon_list.components.PokemonListItem
import com.beworld.task1.common.Constants
import com.beworld.task1.pokemosso.Pokemosso
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun PokemonsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PokemonsViewModel = hiltViewModel()
) {
    Box(modifier) {
        val state = viewModel.pokemonsScreenStateHolder.value
        val downloadState = viewModel.downloadStateHolder.value
        LazyColumn(
            verticalArrangement = Arrangement
                .spacedBy(dimensionResource(id = R.dimen.medium_padding))
        ) {
            items(state.pokemons) { pokemon ->
                var pokemonBitmap by remember {
                    mutableStateOf<ImageBitmap?>(null)
                }
                var isError by remember { mutableStateOf(false) }

                val url = Constants.FRONT_DEFAULT_IMG_URL + pokemon.id + Constants.IMG_FORMAT
                Pokemosso.get()
                    .load(url)
                    .resize(96, 96)
                    .get(object : Pokemosso.Callback {
                        override fun onComplete(bitmap: ImageBitmap?) {
                            pokemonBitmap = bitmap
                        }

                        override fun onError() {
                            isError = true
                        }
                    })

                PokemonListItem(
                    pokemon = pokemon,
                    imageBitmap = pokemonBitmap,
                    isError = isError,
                    onItemClick = {
                        navController
                            .navigate(ApplicationScreens.PokemonDetail.name + "/${pokemon.name}")
                    },
                    onLongItemClick = { viewModel.savePokemon(pokemon.name) }
                )
            }
        }

        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (downloadState.isDownloading) {
            DownloadMessage()
        }
        if (downloadState.downloadingResult != null) {
            DownloadResultMessage(isCorrect = downloadState.downloadingResult)
        }
    }
}