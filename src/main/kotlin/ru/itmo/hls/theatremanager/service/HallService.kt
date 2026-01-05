package ru.itmo.hls.theatremanager.service

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import ru.itmo.hls.theatremanager.entity.Hall
import ru.itmo.hls.theatremanager.repository.HallRepository

@Service
class HallService(
    private val hallRepository: HallRepository
) {
    suspend fun save(hall: Hall): Hall = hallRepository.save(hall)

    suspend fun findHallById(id: Long): Hall? = hallRepository.findById(id)

    suspend fun existsById(id: Long): Boolean = hallRepository.existsById(id)

    suspend fun deleteById(id: Long) = hallRepository.deleteById(id)

    fun findAllByTheatreId(theatreId: Long): Flow<Hall> = hallRepository.findAllByTheatreId(theatreId)
}
