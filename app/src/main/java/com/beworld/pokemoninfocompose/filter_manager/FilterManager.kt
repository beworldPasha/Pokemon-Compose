package com.beworld.pokemoninfocompose.filter_manager


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.beworld.task1.pokemosso.Pokemosso

class FilterManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val uri: String? = inputData.getString(KEY_INPUT_BITMAP)
        val inputName = inputData.getString(KEY_INPUT_NAME)

        if (uri != null && inputName != null) {
            Pokemosso.get()
                .load(uri).get(object : Pokemosso.Callback {
                    override fun onComplete(bitmap: ImageBitmap?) {
                        bitmap?.let {
                            val output = convertToBlackAndWhite(it.asAndroidBitmap())
                            Pokemosso.get()
                                .load(output)
                                .intoDevice(
                                    photoName = inputName,
                                    callback = null
                                )
                        }
                    }

                })
            return Result.success()
        }

        return Result.failure()
    }

    private fun convertToBlackAndWhite(inputBitmap: Bitmap): Bitmap {
        val outputBitmap =
            Bitmap.createBitmap(inputBitmap.width, inputBitmap.height, inputBitmap.config)
        val canvas = Canvas(outputBitmap)
        val paint = android.graphics.Paint()
        val colorMatrix = android.graphics.ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorFilter = android.graphics.ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorFilter
        canvas.drawBitmap(inputBitmap, 0f, 0f, paint)
        return outputBitmap
    }

    companion object {
        const val KEY_INPUT_BITMAP = "input_bitmap"
        const val KEY_INPUT_NAME = "input_name"
    }
}
