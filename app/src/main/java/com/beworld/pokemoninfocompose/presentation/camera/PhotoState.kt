package com.beworld.pokemoninfocompose.presentation.camera

import com.beworld.task1.data.local.photo_provider.Photo

data class PhotosState(
    val isLoading: Boolean = false,
    val photos: MutableList<Photo> = mutableListOf(),
    val error: String = ""
)