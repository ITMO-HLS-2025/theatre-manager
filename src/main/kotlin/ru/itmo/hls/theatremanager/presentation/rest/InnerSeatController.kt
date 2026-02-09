package ru.itmo.hls.theatremanager.presentation.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.itmo.hls.theatremanager.application.dto.SeatPriceDto
import ru.itmo.hls.theatremanager.application.usecase.SeatPriceService

@RestController
@RequestMapping("/inner")
class InnerSeatController(
    private val seatPriceService: SeatPriceService
) {
    @GetMapping("/seats")
    suspend fun getSeatPrices(
        @RequestParam("showId") showId: Long,
        @RequestParam("seats") seats: List<Long>
    ): List<SeatPriceDto> = seatPriceService.findByShowAndSeatIds(showId, seats)
}
