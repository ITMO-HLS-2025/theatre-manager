package ru.itmo.hls.theatremanager.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.itmo.hls.theatremanager.dto.HallCreateDto
import ru.itmo.hls.theatremanager.dto.HallDto
import ru.itmo.hls.theatremanager.dto.TheatreCreatePayload
import ru.itmo.hls.theatremanager.dto.TheatreDto
import ru.itmo.hls.theatremanager.dto.TheatrePayload
import ru.itmo.hls.theatremanager.dto.TheatreViewDto
import ru.itmo.hls.theatremanager.service.TheatreService
import ru.itmo.hls.theatremanager.validation.PaginationValidator

@RestController
@RequestMapping("/api/theatres")
class TheatreController(
    private val theatreService: TheatreService,
    private val paginationValidator: PaginationValidator
) {

    @GetMapping
    suspend fun getAllTheatres(
        @RequestParam city: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<List<TheatreViewDto>> {
        paginationValidator.validateSize(size)

        val resultPage = theatreService.getAllTheatreInCity(city, page, size)

        return ResponseEntity.ok()
            .header("X-Total-Count", resultPage.totalElements.toString())
            .header("X-Page-Number", resultPage.pageNumber.toString())
            .header("X-Page-Size", resultPage.pageSize.toString())
            .body(resultPage.content)
    }

    @GetMapping("/{id}")
    suspend fun getTheatreInfo(@PathVariable id: Long): TheatreDto =
        theatreService.getTheatreInfo(id)

    @DeleteMapping("/{id}")
    suspend fun deleteTheatre(@PathVariable id: Long) {
        theatreService.deleteTheatre(id)
    }

    @PostMapping
    suspend fun createTheatre(@RequestBody payload: TheatreCreatePayload): TheatrePayload =
        theatreService.createTheatre(payload)

    @PutMapping
    suspend fun updateTheatre(@RequestBody payload: TheatrePayload): TheatrePayload =
        theatreService.updateTheatre(payload)

    @PostMapping("/{theatreId}/halls")
    suspend fun createHall(@PathVariable theatreId: Long, @RequestBody dto: HallCreateDto): HallDto =
        theatreService.createHall(theatreId, dto)

    @PutMapping("/halls")
    suspend fun updateHall(@RequestBody dto: HallDto): HallDto =
        theatreService.updateHall(dto)

    @DeleteMapping("/halls/{id}")
    suspend fun deleteHall(@PathVariable id: Long) {
        theatreService.deleteHall(id)
    }
}
