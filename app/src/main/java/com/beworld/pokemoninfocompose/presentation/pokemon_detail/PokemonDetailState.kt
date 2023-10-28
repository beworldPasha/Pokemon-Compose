package com.beworld.pokemoninfocompose.presentation.pokemon_detail

import com.beworld.task1.domain.model.PokemonDetail

data class PokemonDetailState(
    val isLoading: Boolean = false,
    val pokemon: PokemonDetail? = null,
    val error: String = ""
)
