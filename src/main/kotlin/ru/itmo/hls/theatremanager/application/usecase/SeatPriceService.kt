package ru.itmo.hls.theatremanager.application.usecase

import org.springframework.stereotype.Service
import ru.itmo.hls.theatremanager.application.dto.SeatPriceDto
import ru.itmo.hls.theatremanager.application.dto.SeatPricePayload
import ru.itmo.hls.theatremanager.domain.model.Seat
import ru.itmo.hls.theatremanager.domain.model.SeatPrice
import ru.itmo.hls.theatremanager.domain.port.SeatPriceRepository

@Service
class SeatPriceService(
    private val seatPriceRepository: SeatPriceRepository,
    private val seatService: SeatService
) {
    suspend fun findByShowAndSeatIds(showId: Long, seats: List<Long>): List<SeatPriceDto> {
        val prices = seatPriceRepository.findSeatPricesByShowAndSeatIds(showId, seats)
        if (prices.isEmpty()) return emptyList()

        val seatsById = seatService.findAllByIds(prices.map { it.seatId }.distinct())
            .associateBy { it.id }

        return prices.mapNotNull { price ->
            val seat = seatsById[price.seatId] ?: return@mapNotNull null
            seat.toSeatPriceDto(price.price)
        }
    }

    suspend fun deleteByHallId(hallId: Long): Int =
        seatPriceRepository.deleteByHallId(hallId)

    suspend fun replaceShowPrices(showId: Long, prices: List<SeatPricePayload>): List<SeatPriceDto> {
        seatPriceRepository.replaceByShowId(
            showId,
            prices.map { seatPrice -> SeatPrice(seatPrice.seatId, showId, seatPrice.price) }
        )
        val seatIds = prices.map { it.seatId }
        return findByShowAndSeatIds(showId, seatIds)
    }
}

private fun Seat.toSeatPriceDto(price: Int): SeatPriceDto =
    SeatPriceDto(
        id = id ?: 0L,
        raw = rowNumber,
        number = seatNumber,
        price = price
    )
