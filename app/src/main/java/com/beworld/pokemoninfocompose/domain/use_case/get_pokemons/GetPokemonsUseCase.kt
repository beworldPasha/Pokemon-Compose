package com.beworld.task1.domain.use_case.get_pokemons

import com.beworld.task1.common.Constants
import com.beworld.task1.common.Resource
import com.beworld.task1.data.local.database.PokemonEntity
import com.beworld.task1.data.local.database.dao.PokemonDao
import com.beworld.task1.data.mappers.toPokemon
import com.beworld.task1.data.mappers.toPokemonDetail
import com.beworld.task1.domain.model.Pokemon
import com.beworld.task1.domain.model.PokemonDetail
import com.beworld.task1.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPokemonsUseCase @Inject constructor(
    private val repository : PokemonRepository
) {
    operator fun invoke() : Flow<Resource<List<Pokemon>>> = flow {
        try {
            emit(Resource.Loading())
            val pokemons = repository.getPokemons().map {
                it.toPokemon()
            }
            emit(Resource.Success(pokemons))
        } catch (e : Exception) {
            try {
                val pokemons = getFromLocal()
                emit(Resource.Success(pokemons))
            } catch (e : IOException) {
                emit(Resource.Error(Constants.CONNECT_ERROR_MSG))
            }
        }
    }

    private suspend fun getFromLocal(): List<Pokemon> {
        val pokemons = repository.getLocalPokemons()
        if (pokemons.isEmpty()) throw IOException()

        return pokemons.map { it.toPokemon() }
    }
}