package ru.itmo.hls.theatremanager.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.hls.theatremanager.dto.HallCreateDto
import ru.itmo.hls.theatremanager.dto.HallDto
import ru.itmo.hls.theatremanager.dto.TheatreCreatePayload
import ru.itmo.hls.theatremanager.dto.TheatreDto
import ru.itmo.hls.theatremanager.dto.TheatrePage
import ru.itmo.hls.theatremanager.dto.TheatrePayload
import ru.itmo.hls.theatremanager.entity.Hall
import ru.itmo.hls.theatremanager.entity.Seat
import ru.itmo.hls.theatremanager.exception.HallNotFoundException
import ru.itmo.hls.theatremanager.exception.TheatreNotFoundException
import ru.itmo.hls.theatremanager.mapper.toDto
import ru.itmo.hls.theatremanager.mapper.toEntity
import ru.itmo.hls.theatremanager.mapper.toPayload
import ru.itmo.hls.theatremanager.mapper.toViewDto
import ru.itmo.hls.theatremanager.repository.TheatreRepository
import java.time.LocalDateTime

@Service
class TheatreService(
    private val theatreRepository: TheatreRepository,
    private val showService: ShowService,
    private val hallService: HallService,
    private val seatService: SeatService,
    private val seatPriceService: SeatPriceService
) {

    private val log = LoggerFactory.getLogger(TheatreService::class.java)

    suspend fun getAllTheatreInCity(city: String, page: Int, pageSize: Int): TheatrePage {
        log.info("Получение всех театров в городе={}, страница={}, размер страницы={}", city, page, pageSize)
        val offset = (page - 1) * pageSize
        val theatres = theatreRepository.findAllByCity(city, pageSize, offset.toLong())
            .map { it.toViewDto() }
            .toList()
        val total = theatreRepository.countByCity(city)
        log.info("Найдено театров: {}", total)
        return TheatrePage(
            content = theatres,
            totalElements = total,
            pageNumber = page,
            pageSize = pageSize
        )
    }

    suspend fun getTheatreInfo(id: Long): TheatreDto {
        log.info("Получение информации о театре id={}", id)
        val theatre = theatreRepository.findById(id)
            ?: throw TheatreNotFoundException("Theatre not found with id $id")

        val theatreId = theatre.id ?: throw TheatreNotFoundException("Theatre not found with id $id")
        val halls = hallService.findAllByTheatreId(theatreId)
            .map { it.toViewDto() }
            .toList()
        val shows = showService.findAllByTheatreId(
            theatreId,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(14)
        )
        log.info("Найдено {} шоу для театра id={}", shows.size, id)
        return theatre.toDto(halls, shows)
    }

    @Transactional
    suspend fun createTheatre(theatre: TheatreCreatePayload): TheatrePayload {
        log.info("Создание театра с названием={}", theatre.name)
        val savedTheatre = theatreRepository.save(theatre.toEntity())
        val theatreId = savedTheatre.id ?: throw TheatreNotFoundException("Theatre id not generated")
        val halls = theatre.halls.map { hallPayload ->
            hallService.save(Hall(number = hallPayload.number, theatreId = theatreId))
        }
        val payload = savedTheatre.toPayload(halls.map { it.toViewDto() })
        log.info("Театр создан с id={}", payload.id)
        return payload
    }

    @Transactional
    suspend fun updateTheatre(theatre: TheatrePayload): TheatrePayload {
        log.info("Обновление театра id={}", theatre.id)
        if (!theatreRepository.existsById(theatre.id)) {
            throw TheatreNotFoundException("Theatre not found with id ${theatre.id}")
        }
        val updated = theatreRepository.save(theatre.toEntity())
        val theatreId = updated.id ?: throw TheatreNotFoundException("Theatre not found with id ${theatre.id}")
        val updatedHalls = theatre.halls.mapNotNull { hallDto ->
            val hallId = hallDto.id ?: return@mapNotNull null
            hallService.save(Hall(id = hallId, number = hallDto.number, theatreId = theatreId))
        }
        val halls = if (updatedHalls.isNotEmpty()) {
            updatedHalls.map { it.toViewDto() }
        } else {
            hallService.findAllByTheatreId(theatreId)
                .map { it.toViewDto() }
                .toList()
        }
        val payload = updated.toPayload(halls)
        log.info("Театр обновлен id={}", payload.id)
        return payload
    }

    @Transactional
    suspend fun deleteTheatre(id: Long) {
        log.info("Удаление театра id={}", id)
        val halls = hallService.findAllByTheatreId(id).toList()
        halls.forEach { hall ->
            val hallId = hall.id ?: return@forEach
            seatPriceService.deleteByHallId(hallId)
            seatService.deleteByHallId(hallId)
            hallService.deleteById(hallId)
        }
        theatreRepository.deleteById(id)
    }

    @Transactional
    suspend fun createHall(theatreId: Long, dto: HallCreateDto): HallDto {
        val theatre = theatreRepository.findById(theatreId)
            ?: throw TheatreNotFoundException("Theatre not found with id $theatreId")

        val resolvedTheatreId = theatre.id ?: throw TheatreNotFoundException("Theatre not found with id $theatreId")
        val hall = Hall(number = dto.number, theatreId = resolvedTheatreId)
        val savedHall = hallService.save(hall)

        val hallId = savedHall.id ?: throw HallNotFoundException("Hall not created for theatre $theatreId")
        dto.seatRows.forEach { row ->
            row.seats.forEach { seat ->
                seatService.save(
                    Seat(
                        rowNumber = row.row,
                        seatNumber = seat.number,
                        hallId = hallId
                    )
                )
            }
        }

        return savedHall.toDto(dto.seatRows)
    }

    @Transactional
    suspend fun updateHall(dto: HallDto): HallDto {
        val hallId = dto.id ?: throw HallNotFoundException("Hall id is required")
        val hall = hallService.findHallById(hallId)
            ?: throw HallNotFoundException("Hall not found with id $hallId")

        val updatedHall = hallService.save(hall.copy(number = dto.number))
        val updatedHallId = updatedHall.id ?: throw HallNotFoundException("Hall not found with id $hallId")
        seatPriceService.deleteByHallId(updatedHallId)
        seatService.deleteByHallId(updatedHallId)
        dto.seatRows.forEach { row ->
            row.seats.forEach { seat ->
                seatService.save(
                    Seat(
                        rowNumber = row.row,
                        seatNumber = seat.number,
                        hallId = updatedHallId
                    )
                )
            }
        }
        return updatedHall.toDto(dto.seatRows)
    }

    @Transactional
    suspend fun deleteHall(id: Long) {
        if (!hallService.existsById(id)) throw HallNotFoundException("Hall not found with id $id")
        seatPriceService.deleteByHallId(id)
        seatService.deleteByHallId(id)
        hallService.deleteById(id)
    }
}
