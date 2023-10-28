package com.beworld.task1.pokemosso

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.beworld.task1.common.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

class Pokemosso(
    private val context: Context
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        instance = Pokemosso(context)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        instance = null
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: Pokemosso? = null
        fun get(): Pokemosso = instance!!
    }

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val networkDispatcher: CoroutineDispatcher =
        newSingleThreadContext("Network")

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val diskDispatcher: CoroutineDispatcher =
        newSingleThreadContext("Disk")

    private val mainScope = CoroutineScope(Dispatchers.Main)

    interface Callback {
        fun onError() {}
        fun onComplete(bitmap: ImageBitmap?)
        fun onLoading() {}
    }

    inner class Operation(
        private val deferredBitmap: Deferred<Bitmap?>,
        private val callback: Callback? = null
    ) {
        fun get(callback: Callback?) {
            callback?.onLoading()
            var bitmap: Bitmap? = null
            mainScope.launch {
                bitmap = deferredBitmap.await()
                if (bitmap == null) callback?.onError()
            }.invokeOnCompletion { exception ->
                if (exception == null) callback?.onComplete(bitmap?.asImageBitmap())
                else callback?.onError()
            }
        }

        fun intoDevice(photoName: String = "", callback: Callback?) {
            callback?.onLoading()
            mainScope.launch(diskDispatcher) {
                try {
                    deferredBitmap.await()?.also { bitmap ->
                        val baseUri = MediaStore.Images.Media
                            .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

                        val name = SimpleDateFormat(Constants.FILENAME, Locale.ENGLISH)
                            .format(System.currentTimeMillis()) + Constants.IMG_FORMAT

                        val contentValues = ContentValues().apply {
                            put(MediaStore.Images.Media.DISPLAY_NAME, photoName.ifEmpty { name })
                            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/photos")
                        }
                        context.contentResolver.insert(baseUri, contentValues)?.let {
                            context.contentResolver.openOutputStream(it)?.use { stream ->
                                bitmap.compress(
                                    Bitmap.CompressFormat.PNG,
                                    100,
                                    stream
                                )
                            }
                            withContext(Dispatchers.Main) { callback?.onComplete(null) }
                        }
                    } ?: throw Exception("null object")

                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) { callback?.onError() }
                }
            }
        }

        fun resize(dpWidth: Int, dpHeight: Int): Operation {
            val deferredBitmap = mainScope.async(Dispatchers.IO) {
                deferredBitmap.await()?.also {
                    val widthPixels =
                        (dpWidth * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))
                    val heightPixels =
                        (dpHeight * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))

                    return@async Bitmap
                        .createScaledBitmap(it, widthPixels, heightPixels, true)
                }
                return@async null
            }

            return Operation(deferredBitmap)
        }
    }

    fun load(uri: String): Operation {
        val deferredBitmap: Deferred<Bitmap?> = if ("https" in uri) {
            getBitmapFromInternet(uri)
        } else getBitmapFromDevice(Uri.parse(uri))

        return Operation(deferredBitmap)
    }

    fun load(photoBitMap: Bitmap): Operation {
        val deferredBitmap = mainScope.async(diskDispatcher) {
            return@async photoBitMap
        }

        return Operation(deferredBitmap)
    }

    private fun getBitmapFromInternet(url: String): Deferred<Bitmap?> {
        return mainScope.async(networkDispatcher) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()

                return@async BitmapFactory.decodeStream(connection.inputStream)
            } catch (e: Exception) {
                return@async null
            }
        }
    }

    private fun getBitmapFromDevice(uri: Uri): Deferred<Bitmap?> {
        return mainScope.async(diskDispatcher) {
            try {
                return@async BitmapFactory.decodeStream(
                    context.contentResolver.openInputStream(uri)
                )
            } catch (e: Exception) {
                return@async null
            }
        }
    }
}