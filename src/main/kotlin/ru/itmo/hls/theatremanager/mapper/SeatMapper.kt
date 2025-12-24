package ru.itmo.hls.theatremanager.mapper

import ru.itmo.hls.theatremanager.dto.SeatDto
import ru.itmo.hls.theatremanager.entity.Seat


//fun SeatPrice.toSeatStatusDto(ticket: Ticket?): SeatStatusDto = SeatStatusDto(
//    status = mapTicketToSeatStatus(ticket),
//    id = seat.id,
//    number = seat.seatNumber,
//    price = price
//)

fun Seat.toSeatDto(): SeatDto = SeatDto(id,rowNumber, seatNumber)
//
//private fun mapTicketToSeatStatus(ticket: Ticket?): SeatStatus {
//    if (ticket == null) return SeatStatus.FREE
//    if (ticket.status == TicketStatus.RESERVED || ticket.status == TicketStatus.PAID) return SeatStatus.OCCUPIED
//    return SeatStatus.OCCUPIED
//}
