package ru.itmo.hls.theatremanager.infrastructure.db

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.domain.model.Seat
import ru.itmo.hls.theatremanager.domain.port.SeatRepository

@Repository
class SeatRepositoryAdapter(
    private val seatR2dbcRepository: SeatR2dbcRepository
) : SeatRepository {

    override fun findAllByHallId(hallId: Long): Flow<Seat> =
        seatR2dbcRepository.findAllByHallId(hallId)

    override fun findAllById(ids: Iterable<Long>): Flow<Seat> =
        seatR2dbcRepository.findAllById(ids)

    override suspend fun save(seat: Seat): Seat =
        seatR2dbcRepository.save(seat)

    override suspend fun deleteByHallId(hallId: Long): Int =
        seatR2dbcRepository.deleteByHallId(hallId)
}
