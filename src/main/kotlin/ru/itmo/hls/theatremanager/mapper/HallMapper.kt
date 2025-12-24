package ru.itmo.hls.theatremanager.mapper

import ru.itmo.hls.theatremanager.dto.HallCreatePayload
import ru.itmo.hls.theatremanager.dto.HallDto
import ru.itmo.hls.theatremanager.dto.HallViewDto
import ru.itmo.hls.theatremanager.dto.SeatRawDto
import ru.itmo.hls.theatremanager.entity.Hall
import ru.itmo.hls.theatremanager.entity.Theatre


fun Hall.toViewDto() : HallViewDto =
    HallViewDto(
        id = id,
        number = number
    )

fun HallViewDto.toEntity(theatre : Theatre) : Hall =
    Hall(
        id = id ?: 0,
        number = number,
        seats = mutableListOf(),
        theatre = theatre
    )

fun Hall.toDto(seatRaws : List<SeatRawDto>) : HallDto = HallDto(
    id = id,
    number = number,
    seatRows = seatRaws,
)

fun HallCreatePayload.toEntity(theatre: Theatre) : Hall = Hall(
    id = 0,
    number = number,
    seats = mutableListOf(),
    theatre = theatre
)