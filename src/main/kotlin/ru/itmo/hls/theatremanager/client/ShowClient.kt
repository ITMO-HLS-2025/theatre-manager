package ru.itmo.hls.theatremanager.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import ru.itmo.hls.theatremanager.dto.ShowViewDto
import java.time.LocalDateTime

@FeignClient(
    name = "show-manager",
    path = "/api/shows",
    fallback = ShowClientFallback::class
)
interface ShowClient {

    @GetMapping("/theatre/{theatreId}")
    fun findAllByTheatreId(
        @PathVariable theatreId: Long,
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) from: LocalDateTime,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) to: LocalDateTime
    ): List<ShowViewDto>
}
