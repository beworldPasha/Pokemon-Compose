package com.beworld.task1.common

object Constants {
    private const val BASE_POKEMON_IMG_URL =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"

    const val BACK_DEFAULT_IMG_URL = BASE_POKEMON_IMG_URL + "back/"
    const val BACK_SHINY_IMG_URL = BASE_POKEMON_IMG_URL + "back/shiny/"

    const val FRONT_DEFAULT_IMG_URL = BASE_POKEMON_IMG_URL
    const val FRONT_SHINY_IMG_URL = BASE_POKEMON_IMG_URL + "shiny/"

    const val IMG_FORMAT = ".png"

    const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    const val TARGET_PATH = "Pictures/photos/"

    const val DATE_FORMAT = "dd.MM.yyyy HH:mm"

    const val CONNECT_ERROR_MSG = "Loading resources error, try to check your connection."

    const val PARAM_POKEMON_NAME = "pokemonName"
}