package com.beworld.pokemoninfocompose.presentation.application

import android.icu.text.CaseMap.Title
import androidx.annotation.StringRes
import com.beworld.pokemoninfocompose.R

enum class ApplicationScreens(var title: String) {
    PokemonList("Pokemons"),
    PokemonDetail("PokemonDetail"),
    PhotoScreen("Photos"),
    FaqScreen("FAQ")
}