package com.beworld.pokemoninfocompose.presentation.pokemon_list

data class DownloadingState(
    val isDownloading: Boolean = false,
    val downloadingResult: Boolean? = null
)
