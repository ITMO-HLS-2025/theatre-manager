package ru.itmo.hls.theatremanager.client

import org.springframework.stereotype.Component
import ru.itmo.hls.theatremanager.dto.ShowViewDto
import java.time.LocalDateTime

@Component
class ShowClientFallback : ShowClient {
    override fun findAllByTheatreId(theatreId: Long, from: LocalDateTime, to: LocalDateTime): List<ShowViewDto> {
        return emptyList()
    }
}
