package ru.itmo.hls.theatremanager.application.mapper

import ru.itmo.hls.theatremanager.application.dto.HallDto
import ru.itmo.hls.theatremanager.application.dto.HallViewDto
import ru.itmo.hls.theatremanager.application.dto.SeatRowCreateDto
import ru.itmo.hls.theatremanager.domain.model.Hall


fun Hall.toViewDto() : HallViewDto =
    HallViewDto(
        id = id,
        number = number,
        theatreId = theatreId
    )

fun Hall.toDto(seatRaws : List<SeatRowCreateDto>) : HallDto = HallDto(
    id = id,
    number = number,
    seatRows = seatRaws,
)
