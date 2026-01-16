package ru.itmo.hls.theatremanager.dto

data class HallViewDto(
    val id: Long?,
    val number: Int
)

data class HallDto(
    val id: Long?,
    val number: Int,
    val seatRows: List<SeatRowCreateDto>
)

data class HallCreateDto(
    val number: Int,
    val seatRows: List<SeatRowCreateDto>
)

data class HallCreatePayload(
    val number: Int
)
