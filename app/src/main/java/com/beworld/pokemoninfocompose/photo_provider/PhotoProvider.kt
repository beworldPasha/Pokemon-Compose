package com.beworld.task1.photo_provider


import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.beworld.task1.common.Constants
import com.beworld.task1.data.local.photo_provider.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class PhotoProvider @Inject constructor(
    private val context: Context
) {
    private val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED
    )
    private val selection = "${MediaStore.Images.Media.DATA} LIKE ?"
    private val selectionArgs = arrayOf("%/Pictures/photos/%")

    suspend fun getPhotos(): List<Photo> = withContext(Dispatchers.IO) {

        context.contentResolver.query(
            uri, projection, selection, selectionArgs, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)

            val photos = mutableListOf<Photo>()
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val strDate = cursor.getString(dateColumn)

                calendar.timeInMillis = strDate.toLong() * 1000
                val date = dateFormat.format(calendar.time)

                ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                ).also { photos.add(Photo(name, it.toString(), date)) }
            }
            return@withContext photos.toImmutableList()
        }
        return@withContext emptyList()
    }

    interface RemoveCallback {
        fun onSuccess()
        fun onError()
    }

    suspend fun removePhoto(photo: Photo, callback: RemoveCallback?) {
        withContext(Dispatchers.IO) {
            try {
                context.contentResolver.delete(
                    Uri.parse(photo.uri),
                    null,
                    null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { callback?.onError() }
            }
        }
        withContext(Dispatchers.Main) { callback?.onSuccess() }
    }
}