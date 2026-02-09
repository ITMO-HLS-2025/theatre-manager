package ru.itmo.hls.theatremanager.infrastructure.db

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.domain.model.Hall
import ru.itmo.hls.theatremanager.domain.port.HallRepository

@Repository
class HallRepositoryAdapter(
    private val hallR2dbcRepository: HallR2dbcRepository
) : HallRepository {

    override fun findAllByTheatreId(theatreId: Long): Flow<Hall> =
        hallR2dbcRepository.findAllByTheatreId(theatreId)

    override suspend fun findById(id: Long): Hall? =
        hallR2dbcRepository.findById(id)

    override suspend fun save(hall: Hall): Hall =
        hallR2dbcRepository.save(hall)

    override suspend fun deleteById(id: Long) {
        hallR2dbcRepository.deleteById(id)
    }

    override suspend fun existsById(id: Long): Boolean =
        hallR2dbcRepository.existsById(id)
}
