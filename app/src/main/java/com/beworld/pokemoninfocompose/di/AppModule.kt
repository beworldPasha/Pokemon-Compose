package com.beworld.task1.di


import android.content.Context
import com.beworld.pokemoninfocompose.data.repository.FilterRepositoryImpl
import com.beworld.pokemoninfocompose.domain.repository.FilterRepository
import com.beworld.task1.data.local.database.dao.PokemonDao
import com.beworld.task1.data.remote.PokemonApi
import com.beworld.task1.data.repository.PokemonRepositoryImpl
import com.beworld.task1.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokemonApi(): PokemonApi {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonRepository(api: PokemonApi, dao: PokemonDao): PokemonRepository =
        PokemonRepositoryImpl(api, dao)

    @Provides
    @Singleton
    fun provideFilterRepository(@ApplicationContext context: Context): FilterRepository =
        FilterRepositoryImpl(context)
}