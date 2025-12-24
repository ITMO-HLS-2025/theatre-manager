package ru.itmo.hls.theatremanager.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.entity.SeatPrice
import ru.itmo.hls.theatremanager.entity.SeatPriceId

@Repository
interface SeatPriceRepository : JpaRepository<SeatPrice, SeatPriceId> {
    @Query("from SeatPrice seatPrice join fetch seatPrice.seat s where seatPrice.id.seatId = :showId")
    fun findSeatsByShow(showId: Long) : List<SeatPrice>;

    @Query("from SeatPrice sp join fetch sp.seat s where s.id in :ids and sp.id.seatId = :showId")
    fun findSeatsByShowIdAndIdIn(showId: Long, ids : List<Long>) : List<SeatPrice>

}
