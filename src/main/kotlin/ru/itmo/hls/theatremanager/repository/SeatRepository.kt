package ru.itmo.hls.theatremanager.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.itmo.hls.theatremanager.entity.Seat
import ru.itmo.hls.theatremanager.entity.SeatPrice


@Repository
interface SeatRepository : JpaRepository<Seat, Long> {
    @Query("from SeatPrice seatPrice join fetch seatPrice.seat s where seatPrice.id.seatId = :seatId")
    fun findSeatsByShow(showId: Long) : List<SeatPrice>;
}
