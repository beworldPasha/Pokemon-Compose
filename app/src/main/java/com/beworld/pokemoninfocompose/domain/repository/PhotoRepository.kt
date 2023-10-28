package com.beworld.task1.domain.repository

import com.beworld.task1.data.local.photo_provider.Photo
import com.beworld.task1.photo_provider.PhotoProvider

interface PhotoRepository {
    suspend fun getPhotos(): List<Photo>
    suspend fun removePhoto(photo: Photo, callback: PhotoProvider.RemoveCallback)
}