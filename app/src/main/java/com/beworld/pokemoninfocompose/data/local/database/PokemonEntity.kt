package com.beworld.task1.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey  val name: String,
    val id: Int,
    val height: Int,
    val weight: Int,
    val experience: Int,
    val photoUri: String?
)
