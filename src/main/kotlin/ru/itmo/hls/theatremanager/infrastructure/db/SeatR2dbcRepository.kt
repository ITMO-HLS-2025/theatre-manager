package ru.itmo.hls.theatremanager.infrastructure.db

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.domain.model.Seat

@Repository
interface SeatR2dbcRepository : CoroutineCrudRepository<Seat, Long> {
    fun findAllByHallId(hallId: Long): Flow<Seat>

    @Query("delete from seat where hall_id = :hallId")
    suspend fun deleteByHallId(hallId: Long): Int
}
