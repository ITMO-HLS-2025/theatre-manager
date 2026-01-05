package ru.itmo.hls.theatremanager.mapper

import ru.itmo.hls.theatremanager.dto.HallViewDto
import ru.itmo.hls.theatremanager.dto.ShowViewDto
import ru.itmo.hls.theatremanager.dto.TheatreCreatePayload
import ru.itmo.hls.theatremanager.dto.TheatreDto
import ru.itmo.hls.theatremanager.dto.TheatrePayload
import ru.itmo.hls.theatremanager.dto.TheatreViewDto
import ru.itmo.hls.theatremanager.entity.Theatre

fun Theatre.toDto(halls: List<HallViewDto>, shows: List<ShowViewDto>): TheatreDto =
    TheatreDto(
        id = id ?: 0,
        name = name,
        city = city,
        address = address,
        halls = halls,
        shows = shows
    )

fun Theatre.toViewDto(): TheatreViewDto = TheatreViewDto(
    id = id ?: 0,
    name = name,
    city = city,
    address = address
)

fun TheatrePayload.toEntity(): Theatre {
    return Theatre(
        id = id,
        name = name,
        city = city,
        address = address
    )
}

fun TheatreCreatePayload.toEntity(): Theatre =
    Theatre(
        name = name,
        city = city,
        address = address
    )

fun Theatre.toPayload(halls: List<HallViewDto>): TheatrePayload = TheatrePayload(
    id = id ?: 0,
    name = name,
    city = city,
    address = address,
    halls = halls
)
