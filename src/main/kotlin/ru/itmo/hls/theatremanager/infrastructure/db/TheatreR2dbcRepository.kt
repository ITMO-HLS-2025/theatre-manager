package ru.itmo.hls.theatremanager.infrastructure.db

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.domain.model.Theatre

@Repository
interface TheatreR2dbcRepository : CoroutineCrudRepository<Theatre, Long> {

    @Query("select * from theatre where city = :city limit :limit offset :offset")
    fun findAllByCity(city: String, limit: Int, offset: Long): Flow<Theatre>

    @Query("select count(*) from theatre where city = :city")
    suspend fun countByCity(city: String): Long
}
