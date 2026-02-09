package ru.itmo.hls.theatremanager.domain.model

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("seat_price")
data class SeatPrice(
    @Column("seat_id")
    val seatId: Long,

    @Column("show_id")
    val showId: Long,

    @Column("price")
    val price: Int
)
