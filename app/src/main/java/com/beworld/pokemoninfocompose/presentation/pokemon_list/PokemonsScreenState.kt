package com.beworld.pokemoninfocompose.presentation.pokemon_list

import com.beworld.task1.domain.model.Pokemon

data class PokemonsScreenState(
    val isLoading: Boolean = false,
    val pokemons: List<Pokemon> = emptyList(),
    val error: String = ""
)