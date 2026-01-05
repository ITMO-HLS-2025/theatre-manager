package ru.itmo.hls.theatremanager.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.entity.Hall

@Repository
interface HallRepository : CoroutineCrudRepository<Hall, Long> {
    fun findAllByTheatreId(theatreId: Long): Flow<Hall>
}
