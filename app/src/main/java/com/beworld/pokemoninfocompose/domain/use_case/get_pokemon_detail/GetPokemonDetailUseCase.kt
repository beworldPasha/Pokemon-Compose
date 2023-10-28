package com.beworld.task1.domain.use_case.get_pokemon_detail

import com.beworld.task1.common.Constants
import com.beworld.task1.common.Resource
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

class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(pokemonName: String) : Flow<Resource<PokemonDetail>> = flow {
        try {
            emit(Resource.Loading())
            val pokemon = repository.getPokemonByName(pokemonName).toPokemonDetail()

            emit(Resource.Success(pokemon))
        } catch (e : Exception) {
            val pokemon = getFromLocal(pokemonName)
            emit(Resource.Success(pokemon))
        } catch (e : IOException) {
            emit(Resource.Error(Constants.CONNECT_ERROR_MSG))
        }
    }

    private suspend fun getFromLocal(pokemonName: String): PokemonDetail {
        val pokemon = repository
            .getLocalPokemonByName(pokemonName) ?: throw IOException()

        return pokemon.toPokemonDetail()
    }
}