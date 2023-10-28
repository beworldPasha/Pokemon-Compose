package com.beworld.pokemoninfocompose.presentation.pokemon_list

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beworld.task1.common.Constants
import com.beworld.task1.common.Resource
import com.beworld.task1.domain.use_case.get_pokemons.GetPokemonsUseCase
import com.beworld.task1.domain.use_case.save_pokemon.SavePokemonUseCase
import com.beworld.task1.pokemosso.Pokemosso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PokemonsViewModel @Inject constructor(
    private val getPokemonsUseCase: GetPokemonsUseCase,
    private val savePokemonUseCase: SavePokemonUseCase
) : ViewModel() {
    private val _pokemonsScreenStateHolder =
        mutableStateOf<PokemonsScreenState>(PokemonsScreenState())
    val pokemonsScreenStateHolder: State<PokemonsScreenState> = _pokemonsScreenStateHolder

    private val _downloadStateHolder = mutableStateOf<DownloadingState>(DownloadingState())
    val downloadStateHolder: State<DownloadingState> = _downloadStateHolder

    init {
        getPokemons()
    }

    private fun getPokemons() {
        getPokemonsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _pokemonsScreenStateHolder.value =
                        PokemonsScreenState(pokemons = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    _pokemonsScreenStateHolder.value =
                        PokemonsScreenState(error = result.message ?: "An expected error")
                }

                is Resource.Loading -> {
                    _pokemonsScreenStateHolder.value = PokemonsScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun savePokemon(pokemonName: String) {
        savePokemonUseCase(pokemonName).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _downloadStateHolder.value = DownloadingState(
                        isDownloading = false,
                        downloadingResult = true
                    )

                    delay(1500)

                    _downloadStateHolder.value = DownloadingState(
                        isDownloading = false,
                        downloadingResult = null
                    )
                }

                is Resource.Error -> {
                    _downloadStateHolder.value = DownloadingState(
                        isDownloading = false,
                        downloadingResult = false
                    )
                }

                is Resource.Loading -> {
                    _downloadStateHolder.value = DownloadingState(
                        isDownloading = true,
                        downloadingResult = null
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
