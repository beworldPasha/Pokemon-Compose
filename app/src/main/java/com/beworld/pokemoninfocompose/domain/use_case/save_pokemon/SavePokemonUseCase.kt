package com.beworld.task1.domain.use_case.save_pokemon


import com.beworld.task1.common.Resource
import com.beworld.task1.data.mappers.toPokemonDetail
import com.beworld.task1.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavePokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(pokemonName: String) = flow {
        try {
            emit(Resource.Loading(true))
            val pokemon = repository
                .getPokemonByName(pokemonName).toPokemonDetail()
            repository.addPokemonToDatabase(pokemon)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error("Error: Pokemon not saved"))
        }
    }
}