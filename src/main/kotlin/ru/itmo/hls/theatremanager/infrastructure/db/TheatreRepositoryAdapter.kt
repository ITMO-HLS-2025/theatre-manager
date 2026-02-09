package ru.itmo.hls.theatremanager.infrastructure.db

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.domain.model.Theatre
import ru.itmo.hls.theatremanager.domain.port.TheatreRepository

@Repository
class TheatreRepositoryAdapter(
    private val theatreR2dbcRepository: TheatreR2dbcRepository
) : TheatreRepository {

    override fun findAllByCity(city: String, limit: Int, offset: Long): Flow<Theatre> =
        theatreR2dbcRepository.findAllByCity(city, limit, offset)

    override suspend fun countByCity(city: String): Long =
        theatreR2dbcRepository.countByCity(city)

    override suspend fun findById(id: Long): Theatre? =
        theatreR2dbcRepository.findById(id)

    override fun findAllById(ids: Iterable<Long>): Flow<Theatre> =
        theatreR2dbcRepository.findAllById(ids)

    override suspend fun save(theatre: Theatre): Theatre =
        theatreR2dbcRepository.save(theatre)

    override suspend fun existsById(id: Long): Boolean =
        theatreR2dbcRepository.existsById(id)

    override suspend fun deleteById(id: Long) {
        theatreR2dbcRepository.deleteById(id)
    }
}
