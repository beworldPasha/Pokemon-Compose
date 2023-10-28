package com.beworld.task1.domain.repository

import com.beworld.task1.data.local.database.PokemonEntity
import com.beworld.task1.data.remote.dto.PokemonDetailDto
import com.beworld.task1.data.remote.dto.PokemonDto
import com.beworld.task1.data.remote.dto.PokemonListDto
import com.beworld.task1.domain.model.PokemonDetail

interface PokemonRepository {
    suspend fun getPokemons() : List<PokemonDto>
    suspend fun getPokemonByName(pokemonName : String) : PokemonDetailDto

    suspend fun getLocalPokemons(): List<PokemonEntity>
    suspend fun getLocalPokemonByName(pokemonName: String): PokemonEntity?
    suspend fun addPokemonToDatabase(pokemon: PokemonDetail)
}