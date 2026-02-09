package ru.itmo.hls.theatremanager.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("seat")
data class Seat(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("row_number")
    val rowNumber: Int,

    @Column("seat_number")
    val seatNumber: Int,

    @Column("hall_id")
    val hallId: Long
)
