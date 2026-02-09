package ru.itmo.hls.theatremanager.application.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import ru.itmo.hls.theatremanager.application.port.ShowClient
import ru.itmo.hls.theatremanager.application.dto.ShowViewDto
import java.time.LocalDateTime

@Service
class ShowService(
    private val showClient: ShowClient
) {
    suspend fun findAllByTheatreId(
        theatreId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<ShowViewDto> = withContext(Dispatchers.IO) {
        showClient.findAllByTheatreId(theatreId, from, to)
    }
}
