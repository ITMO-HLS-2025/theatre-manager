package ru.itmo.hls.theatremanager.controller

import kotlinx.coroutines.flow.toList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.hls.theatremanager.dto.HallViewDto
import ru.itmo.hls.theatremanager.dto.SeatRowViewDto
import ru.itmo.hls.theatremanager.dto.SeatStatus
import ru.itmo.hls.theatremanager.dto.SeatStatusViewDto
import ru.itmo.hls.theatremanager.exception.HallNotFoundException
import ru.itmo.hls.theatremanager.mapper.toViewDto
import ru.itmo.hls.theatremanager.service.HallService
import ru.itmo.hls.theatremanager.service.SeatService

@RestController
@RequestMapping("/api/halls")
class HallController(
    private val hallService: HallService,
    private val seatService: SeatService
) {
    @GetMapping("/{id}")
    suspend fun getHall(@PathVariable id: Long): HallViewDto {
        val hall = hallService.findHallById(id)
            ?: throw HallNotFoundException("Hall not found with id $id")
        return hall.toViewDto()
    }

    @GetMapping("/{id}/seats")
    suspend fun getHallSeats(@PathVariable id: Long): List<SeatRowViewDto> {
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
}
