package ru.itmo.hls.theatremanager.infrastructure.db

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.domain.model.Hall

@Repository
interface HallR2dbcRepository : CoroutineCrudRepository<Hall, Long> {
    fun findAllByTheatreId(theatreId: Long): Flow<Hall>
}
