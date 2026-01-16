package ru.itmo.hls.theatremanager.mapper

import ru.itmo.hls.theatremanager.dto.HallDto
import ru.itmo.hls.theatremanager.dto.HallViewDto
import ru.itmo.hls.theatremanager.dto.SeatRowCreateDto
import ru.itmo.hls.theatremanager.entity.Hall


fun Hall.toViewDto() : HallViewDto =
    HallViewDto(
        id = id,
        number = number
    )

fun Hall.toDto(seatRaws : List<SeatRowCreateDto>) : HallDto = HallDto(
    id = id,
    number = number,
    seatRows = seatRaws,
)
