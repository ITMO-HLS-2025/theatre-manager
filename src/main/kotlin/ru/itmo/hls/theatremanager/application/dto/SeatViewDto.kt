package ru.itmo.hls.theatremanager.application.dto

data class SeatRowViewDto(
    val row: Int,
    val seats: List<SeatStatusViewDto>
)

data class SeatStatusViewDto(
    val seatId: Long,
    val number: Int,
    val status: String
)
