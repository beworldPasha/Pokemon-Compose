package com.beworld.pokemoninfocompose.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.beworld.pokemoninfocompose.domain.repository.FilterRepository
import com.beworld.pokemoninfocompose.filter_manager.FilterManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class FilterRepositoryImpl(
    private val context: Context
) : FilterRepository {
    override suspend fun filterToBlackWhite(uri: String, name: String): Boolean {
        val inputData = workDataOf(
            FilterManager.KEY_INPUT_BITMAP to uri,
            FilterManager.KEY_INPUT_NAME to name
        )

        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val colorConversionRequest = OneTimeWorkRequest.Builder(FilterManager::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()


        try {
            WorkManager.getInstance(context).enqueue(colorConversionRequest)
            val workInfo = withContext(Dispatchers.IO) {
                WorkManager
                    .getInstance(context).getWorkInfoById(colorConversionRequest.id).get()
            }
            if (workInfo.state == WorkInfo.State.SUCCEEDED) return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}