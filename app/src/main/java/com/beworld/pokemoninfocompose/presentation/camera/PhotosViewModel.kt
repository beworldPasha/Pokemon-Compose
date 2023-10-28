package com.beworld.pokemoninfocompose.presentation.camera

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beworld.pokemoninfocompose.domain.repository.FilterRepository
import com.beworld.task1.common.Resource
import com.beworld.task1.data.local.photo_provider.Photo
import com.beworld.task1.domain.repository.PhotoRepository
import com.beworld.task1.domain.use_case.get_photos.GetPhotosUseCase
import com.beworld.task1.photo_provider.PhotoProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val photoRepository: PhotoRepository,
    private val filterRepository: FilterRepository
) : ViewModel() {
    private val _photoListStateHolder = mutableStateOf(PhotosState())
    val photoListStateHolder: State<PhotosState> = _photoListStateHolder

    private val _loadingState: MutableState<Boolean?> = mutableStateOf(null)
    val loadingState: State<Boolean?> = _loadingState

    fun loadPhotoList() {
        getPhotosUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _photoListStateHolder.value =
                        PhotosState(photos = result.data ?: mutableListOf())
                }

                is Resource.Error -> {
                    _photoListStateHolder.value =
                        PhotosState(error = result.message ?: "An expected error")
                }

                is Resource.Loading -> {
                    _photoListStateHolder.value = PhotosState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun removePhoto(photo: Photo) {
        viewModelScope.launch {
            photoRepository.removePhoto(photo, object : PhotoProvider.RemoveCallback {
                override fun onSuccess() {
                    _photoListStateHolder.value.photos.remove(photo)
                    loadPhotoList()
                }

                override fun onError() {}

            })
        }
    }

    fun photoToBlackAndWhite(photoUri: String, name: String) {
        viewModelScope.launch {
            _loadingState.value = true
            filterRepository.filterToBlackWhite(photoUri, name)
            delay(1500)
            _loadingState.value = null
            loadPhotoList()
        }
    }
}