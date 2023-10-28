package com.beworld.task1.domain.use_case.get_photos

import com.beworld.task1.common.Constants
import com.beworld.task1.common.Resource
import com.beworld.task1.data.local.photo_provider.Photo
import com.beworld.task1.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    operator fun invoke() : Flow<Resource<MutableList<Photo>>> = flow {
        try {
            emit(Resource.Loading())
            val photos = repository.getPhotos().toMutableList()

            if (photos.isEmpty()) {
                emit(Resource.Error("U didn`t take a photo."))
            } else emit(Resource.Success(photos))
        } catch (e : HttpException) {
            emit(Resource.Error(Constants.CONNECT_ERROR_MSG))
        } catch (e : IOException) {
            emit(Resource.Error(Constants.CONNECT_ERROR_MSG))
        }
    }
}