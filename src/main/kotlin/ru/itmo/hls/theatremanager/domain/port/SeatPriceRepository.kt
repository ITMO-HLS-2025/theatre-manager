package ru.itmo.hls.theatremanager.domain.port

import ru.itmo.hls.theatremanager.domain.model.SeatPrice

interface SeatPriceRepository {
    suspend fun findSeatPricesByShowAndSeatIds(showId: Long, seatIds: List<Long>): List<SeatPrice>
    suspend fun deleteByHallId(hallId: Long): Int
    suspend fun replaceByShowId(showId: Long, prices: List<SeatPrice>): Int
}
