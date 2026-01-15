package ru.itmo.hls.theatremanager.dto

data class SeatRowViewDto(
    val row: Int,
    val seats: List<SeatStatusViewDto>
)

data class SeatStatusViewDto(
    val seatId: Long,
    val number: Int,
    val status: String
)
