package com.beworld.pokemoninfocompose.domain.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

interface FilterRepository {
    suspend fun filterToBlackWhite(uri: String, name: String): Boolean
}