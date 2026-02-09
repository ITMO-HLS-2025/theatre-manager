package ru.itmo.hls.theatremanager.application.dto

data class SeatRowCreateDto(val row: Int, val seats: List<SeatCreateDto>)

data class SeatCreateDto(val number: Int)

data class SeatPricePayload(
    val seatId: Long,
    val price: Int
)

data class SeatPriceDto(
    val id: Long,
    val raw: Int,
    val number: Int,
    val price: Int
)
