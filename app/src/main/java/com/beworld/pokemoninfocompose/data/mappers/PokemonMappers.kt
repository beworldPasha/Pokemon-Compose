package com.beworld.task1.data.mappers

import com.beworld.task1.data.local.database.PokemonEntity
import com.beworld.task1.data.remote.dto.PokemonDetailDto
import com.beworld.task1.data.remote.dto.PokemonDto
import com.beworld.task1.domain.model.Pokemon
import com.beworld.task1.domain.model.PokemonDetail
import java.util.Locale

fun PokemonDetailDto.toPokemonDetail(): PokemonDetail {
    return PokemonDetail(
        id = id,
        name = name.replaceFirstChar { it.uppercaseChar() },
        height = height,
        weight = weight,
        experience = baseExperience,
        photoUrl = sprites.other.officialArtwork.frontDefault
    )
}

fun PokemonDto.toPokemon() =
    Pokemon(
        name = name.replaceFirstChar { it.uppercaseChar() },
        id = Regex("/(\\d+)/$")
            .find(url)?.groupValues?.get(1)?.toInt(),
        fromRemote = true
    )

fun PokemonDetail.toPokemonEntity() = PokemonEntity(
    id = id,
    name = name,
    height = height,
    weight = weight,
    experience = experience,
    photoUri = photoUrl
)

fun PokemonEntity.toPokemonDetail() = PokemonDetail(
    id = id,
    name = name,
    height = height,
    weight = weight,
    experience = experience,
    photoUrl = photoUri ?: ""
)

fun PokemonEntity.toPokemon() = Pokemon(
    id = id,
    name = name,
    fromRemote = false
)