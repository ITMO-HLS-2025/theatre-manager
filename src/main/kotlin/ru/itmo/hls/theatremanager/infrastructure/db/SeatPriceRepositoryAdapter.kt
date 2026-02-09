package ru.itmo.hls.theatremanager.infrastructure.db

import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.domain.model.SeatPrice
import ru.itmo.hls.theatremanager.domain.port.SeatPriceRepository

@Repository
class SeatPriceRepositoryAdapter(
    private val seatPriceDbRepository: SeatPriceDbRepository
) : SeatPriceRepository {

    override suspend fun findSeatPricesByShowAndSeatIds(showId: Long, seatIds: List<Long>): List<SeatPrice> =
        seatPriceDbRepository.findSeatPricesByShowAndSeatIds(showId, seatIds)

    override suspend fun deleteByHallId(hallId: Long): Int =
        seatPriceDbRepository.deleteByHallId(hallId)

    override suspend fun replaceByShowId(showId: Long, prices: List<SeatPrice>): Int =
        seatPriceDbRepository.replaceByShowId(showId, prices)
}
