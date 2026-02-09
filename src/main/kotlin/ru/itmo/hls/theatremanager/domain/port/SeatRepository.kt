package ru.itmo.hls.theatremanager.domain.port

import kotlinx.coroutines.flow.Flow
import ru.itmo.hls.theatremanager.domain.model.Seat

interface SeatRepository {
    fun findAllByHallId(hallId: Long): Flow<Seat>
    fun findAllById(ids: Iterable<Long>): Flow<Seat>
    suspend fun save(seat: Seat): Seat
    suspend fun deleteByHallId(hallId: Long): Int
}
