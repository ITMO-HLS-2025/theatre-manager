package ru.itmo.hls.theatremanager.domain.port

import kotlinx.coroutines.flow.Flow
import ru.itmo.hls.theatremanager.domain.model.Theatre

interface TheatreRepository {
    fun findAllByCity(city: String, limit: Int, offset: Long): Flow<Theatre>
    suspend fun countByCity(city: String): Long
    suspend fun findById(id: Long): Theatre?
    fun findAllById(ids: Iterable<Long>): Flow<Theatre>
    suspend fun save(theatre: Theatre): Theatre
    suspend fun existsById(id: Long): Boolean
    suspend fun deleteById(id: Long)
}
