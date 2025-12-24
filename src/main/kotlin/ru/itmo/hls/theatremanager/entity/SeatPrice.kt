package ru.itmo.hls.theatremanager.entity

import jakarta.persistence.*

@Entity
@Table(name = "seat_price")
class SeatPrice(
    @EmbeddedId
    var id: SeatPriceId,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seatId")
    @JoinColumn(name = "seat_id")
    val seat: Seat,

    val price: Int
)
{
    constructor():this(SeatPriceId(), Seat(), 0);
}

@Embeddable
data class SeatPriceId(
    val seatId: Long = 0,
    val showId: Long = 0
)
