package com.beworld.task1.data.remote

import com.beworld.task1.data.remote.dto.PokemonDetailDto
import com.beworld.task1.data.remote.dto.PokemonDto
import com.beworld.task1.data.remote.dto.PokemonListDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {

    @GET("v2/pokemon")
    suspend fun getPokemons(): PokemonListDto

    @GET("v2/pokemon/{pokemonName}")
    suspend fun getPokemonByName(@Path("pokemonName") pokemonName: String): PokemonDetailDto
}