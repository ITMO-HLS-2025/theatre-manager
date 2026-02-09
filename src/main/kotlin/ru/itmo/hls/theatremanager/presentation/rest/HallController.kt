package ru.itmo.hls.theatremanager.presentation.rest

import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.itmo.hls.theatremanager.application.dto.HallViewDto
import ru.itmo.hls.theatremanager.application.dto.SeatRowViewDto
import ru.itmo.hls.theatremanager.application.dto.SeatPriceDto
import ru.itmo.hls.theatremanager.application.dto.SeatPricePayload
import ru.itmo.hls.theatremanager.application.dto.SeatStatus
import ru.itmo.hls.theatremanager.application.dto.SeatStatusViewDto
import ru.itmo.hls.theatremanager.domain.exception.HallNotFoundException
import ru.itmo.hls.theatremanager.application.mapper.toViewDto
import ru.itmo.hls.theatremanager.application.usecase.HallService
import ru.itmo.hls.theatremanager.application.usecase.SeatPriceService
import ru.itmo.hls.theatremanager.application.usecase.SeatService
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/halls")
class HallController(
    private val hallService: HallService,
    private val seatService: SeatService,
    private val seatPriceService: SeatPriceService
) {
    @GetMapping("/{id}")
    suspend fun getHall(
        @PathVariable id: Long
    ): HallViewDto {
        val hall = hallService.findHallById(id)
            ?: throw HallNotFoundException("Hall not found with id $id")
        return hall.toViewDto()
    }

    @GetMapping("/{id}/seats")
    suspend fun getHallSeats(
        @PathVariable id: Long
    ): List<SeatRowViewDto> {
        val seats = seatService.findAllByHallId(id).toList()
        if (seats.isEmpty()) return emptyList()

        return seats.groupBy { it.rowNumber }
            .toSortedMap()
            .map { (rowNumber, rowSeats) ->
                SeatRowViewDto(
                    row = rowNumber,
                    seats = rowSeats.sortedBy { it.seatNumber }.map { seat ->
                        SeatStatusViewDto(
                            seatId = seat.id ?: 0L,
                            number = seat.seatNumber,
                            status = SeatStatus.FREE.name
                        )
                    }
                )
            }
    }

    @PostMapping("/{id}/prices")
    suspend fun setSeatPrices(
        @PathVariable id: Long,
        @RequestParam showId: Long,
        @RequestBody prices: List<SeatPricePayload>
    ): List<SeatPriceDto> {
        if (prices.isEmpty()) return emptyList()
        val seats = seatService.findAllByHallId(id).toList()
        val allowedSeatIds = seats.mapNotNull { it.id }.toSet()
        val requestedSeatIds = prices.map { it.seatId }
        if (!allowedSeatIds.containsAll(requestedSeatIds)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Some seats do not belong to hall $id"
            )
        }
        return seatPriceService.replaceShowPrices(showId, prices)
    }
}
