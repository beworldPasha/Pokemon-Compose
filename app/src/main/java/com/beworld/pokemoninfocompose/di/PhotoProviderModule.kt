package com.beworld.task1.di

import android.content.Context
import com.beworld.task1.data.repository.PhotoRepositoryImpl
import com.beworld.task1.domain.repository.PhotoRepository
import com.beworld.task1.photo_provider.PhotoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotoProviderModule {
    @Provides
    @Singleton
    fun providePhotoProvider(@ApplicationContext context: Context): PhotoProvider =
        PhotoProvider(context)


    @Provides
    @Singleton
    fun providePhotoRepository(provider: PhotoProvider): PhotoRepository =
        PhotoRepositoryImpl(provider)
}