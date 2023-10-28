package com.beworld.task1.data.repository

import com.beworld.task1.data.local.photo_provider.Photo
import com.beworld.task1.domain.repository.PhotoRepository
import com.beworld.task1.photo_provider.PhotoProvider
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val provider: PhotoProvider
): PhotoRepository {
    override suspend fun getPhotos(): List<Photo> = provider.getPhotos()
    override suspend fun removePhoto(photo: Photo, callback: PhotoProvider.RemoveCallback) {
        provider.removePhoto(photo, callback)
    }
}