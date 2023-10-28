package com.beworld.task1.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beworld.task1.data.local.database.PokemonEntity

@Dao
interface PokemonDao {
    @Query("SELECT * from pokemons")
    suspend fun getAllPokemons(): List<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Query("SELECT * from pokemons WHERE name = :pokemonName")
    suspend fun getPokemonByName(pokemonName: String): PokemonEntity?
}