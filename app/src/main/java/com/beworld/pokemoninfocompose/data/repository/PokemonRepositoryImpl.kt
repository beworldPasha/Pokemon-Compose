package com.beworld.task1.data.repository

import com.beworld.task1.data.local.database.PokemonEntity
import com.beworld.task1.data.local.database.dao.PokemonDao
import com.beworld.task1.data.mappers.toPokemonEntity
import com.beworld.task1.data.remote.PokemonApi
import com.beworld.task1.data.remote.dto.PokemonDetailDto
import com.beworld.task1.data.remote.dto.PokemonDto
import com.beworld.task1.domain.model.PokemonDetail
import com.beworld.task1.domain.repository.PokemonRepository
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi,
    private val dao: PokemonDao
) : PokemonRepository {
    override suspend fun getPokemons(): List<PokemonDto> =
        api.getPokemons().results

    override suspend fun getPokemonByName(pokemonName: String): PokemonDetailDto =
        api.getPokemonByName(pokemonName.lowercase())

    override suspend fun getLocalPokemons(): List<PokemonEntity> =
        dao.getAllPokemons()


    override suspend fun getLocalPokemonByName(pokemonName: String): PokemonEntity? =
        dao.getPokemonByName(pokemonName)


    override suspend fun addPokemonToDatabase(pokemon: PokemonDetail) {
        dao.insertPokemon(pokemon.toPokemonEntity())
    }
}