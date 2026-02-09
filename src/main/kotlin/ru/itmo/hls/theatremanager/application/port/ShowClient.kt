package ru.itmo.hls.theatremanager.application.port

import ru.itmo.hls.theatremanager.application.dto.ShowViewDto
import java.time.LocalDateTime

interface ShowClient {
    fun findAllByTheatreId(theatreId: Long, from: LocalDateTime, to: LocalDateTime): List<ShowViewDto>
}
