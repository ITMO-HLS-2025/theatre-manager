package ru.itmo.hls.theatreservice.mapper

import ru.itmo.hls.theatreservice.dto.*
import ru.itmo.hls.theatreservice.entity.Theatre
import kotlin.collections.map

fun Theatre.toDto(shows: List<ShowViewDto>): TheatreDto =
    TheatreDto(
        id = id,
        name = name,
        city = city,
        address = address,
        halls = halls.map { it.toViewDto() },
        shows = shows
    )

fun Theatre.toViewDto(): TheatreViewDto = TheatreViewDto(
    id = id,
    name = name,
    city = city,
    address = address
)

fun TheatrePayload.toEntity(): Theatre {
    val theatre = Theatre(
        id = id,
        name = name,
        city = city,
        address = address,
        halls = mutableListOf()
    )
    theatre.halls = halls.map { it.toEntity(theatre) }.toMutableList()
    return theatre
}

fun TheatreCreatePayload.toEntity() : Theatre
{
    val theatre = Theatre(
        name = name,
        city = city,
        address = address,
        halls = mutableListOf()
    )
    theatre.halls = halls.map { it.toEntity(theatre) }.toMutableList()
    return theatre
}

fun Theatre.toPayload(): TheatrePayload = TheatrePayload(
    id = id,
    name = name,
    city = city,
    address = address,
    halls = halls.map { it.toViewDto() }
)