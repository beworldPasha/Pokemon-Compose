package com.beworld.task1.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beworld.task1.data.local.database.dao.PokemonDao

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}