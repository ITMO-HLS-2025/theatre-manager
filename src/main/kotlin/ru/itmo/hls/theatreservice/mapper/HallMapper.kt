package ru.itmo.hls.theatreservice.mapper

import ru.itmo.hls.theatreservice.dto.HallCreatePayload
import ru.itmo.hls.theatreservice.dto.HallDto
import ru.itmo.hls.theatreservice.dto.HallViewDto
import ru.itmo.hls.theatreservice.dto.SeatRawDto
import ru.itmo.hls.theatreservice.entity.Hall
import ru.itmo.hls.theatreservice.entity.Theatre


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