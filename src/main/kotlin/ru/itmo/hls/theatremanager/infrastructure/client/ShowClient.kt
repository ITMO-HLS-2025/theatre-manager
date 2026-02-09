package ru.itmo.hls.theatremanager.infrastructure.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import ru.itmo.hls.theatremanager.application.dto.ShowViewDto
import java.time.LocalDateTime

@FeignClient(
    name = "show-manager",
    path = "/api/shows",
    fallback = ShowClientFallback::class
)
interface ShowClient : ru.itmo.hls.theatremanager.application.port.ShowClient {

    @GetMapping("/theatre/{theatreId}")
    override fun findAllByTheatreId(
        @PathVariable theatreId: Long,
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) from: LocalDateTime,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) to: LocalDateTime
    ): List<ShowViewDto>
}
