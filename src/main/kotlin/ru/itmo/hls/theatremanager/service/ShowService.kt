package ru.itmo.hls.theatremanager.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import ru.itmo.hls.theatremanager.client.ShowClient
import ru.itmo.hls.theatremanager.dto.ShowViewDto
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
