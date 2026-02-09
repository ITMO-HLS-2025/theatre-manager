package ru.itmo.hls.theatremanager.application.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import ru.itmo.hls.theatremanager.domain.model.Seat
import ru.itmo.hls.theatremanager.domain.port.SeatRepository

@Service
class SeatService(
    private val seatRepository: SeatRepository
) {
    suspend fun save(seat: Seat): Seat = seatRepository.save(seat)

    suspend fun deleteByHallId(hallId: Long): Int = seatRepository.deleteByHallId(hallId)

    fun findAllByHallId(hallId: Long): Flow<Seat> = seatRepository.findAllByHallId(hallId)

    suspend fun findAllByIds(ids: List<Long>): List<Seat> =
        seatRepository.findAllById(ids).toList()
}
