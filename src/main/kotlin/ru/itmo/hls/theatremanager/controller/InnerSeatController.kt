package ru.itmo.hls.theatremanager.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.itmo.hls.theatremanager.dto.SeatPriceDto
import ru.itmo.hls.theatremanager.service.SeatPriceService

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
