package com.beworld.task1.data.remote.dto


import com.google.gson.annotations.SerializedName

data class PastType(
    val generation: Generation,
    val types: List<Type>
)