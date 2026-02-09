package ru.itmo.hls.theatremanager.domain.port

import kotlinx.coroutines.flow.Flow
import ru.itmo.hls.theatremanager.domain.model.Hall

interface HallRepository {
    fun findAllByTheatreId(theatreId: Long): Flow<Hall>
    suspend fun findById(id: Long): Hall?
    suspend fun save(hall: Hall): Hall
    suspend fun deleteById(id: Long)
    suspend fun existsById(id: Long): Boolean
}
