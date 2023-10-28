package com.beworld.pokemoninfocompose.presentation.pokemon_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beworld.pokemoninfocompose.domain.repository.FilterRepository
import com.beworld.task1.common.Constants
import com.beworld.task1.common.Resource
import com.beworld.task1.domain.use_case.get_pokemon_detail.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val useCase: GetPokemonDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _pokemonDetailStateHolder = mutableStateOf<PokemonDetailState>(PokemonDetailState())
    val pokemonDetailStateHolder: State<PokemonDetailState> = _pokemonDetailStateHolder

    init {
        savedStateHandle.get<String>(Constants.PARAM_POKEMON_NAME) ?.let { pokemonName ->
            fetchPokemon(pokemonName)
        }
    }


    private fun fetchPokemon(pokemonName: String) {
        useCase(pokemonName).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _pokemonDetailStateHolder.value =
                        PokemonDetailState(pokemon = result.data)
                }

                is Resource.Error -> {
                    _pokemonDetailStateHolder.value =
                        PokemonDetailState(error = result.message ?: "An expected error")
                }

                is Resource.Loading -> {
                    _pokemonDetailStateHolder.value = PokemonDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}