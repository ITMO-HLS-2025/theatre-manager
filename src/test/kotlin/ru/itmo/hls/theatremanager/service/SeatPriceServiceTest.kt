package ru.itmo.hls.theatremanager.service

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import ru.itmo.hls.theatremanager.entity.Seat
import ru.itmo.hls.theatremanager.entity.SeatPrice
import ru.itmo.hls.theatremanager.dto.SeatPriceDto
import ru.itmo.hls.theatremanager.repository.SeatPriceRepository

class SeatPriceServiceTest {

    @Test
    @DisplayName("Non-empty seat list returns repository result")
    fun nonEmptySeats() = runBlocking {
        val repository = mock<SeatPriceRepository>()
        val seatService = mock<SeatService>()
        val service = SeatPriceService(repository, seatService)
        val prices = listOf(
            SeatPrice(seatId = 1L, showId = 1L, price = 1000)
        )
        val seats = listOf(
            Seat(id = 1L, rowNumber = 1, seatNumber = 1, hallId = 1L)
        )

        whenever(repository.findSeatPricesByShowAndSeatIds(1L, listOf(1L))).thenReturn(prices)
        whenever(seatService.findAllByIds(listOf(1L))).thenReturn(seats)

        val result = service.findByShowAndSeatIds(1L, listOf(1L))

        assertEquals(
            listOf(SeatPriceDto(id = 1, raw = 1, number = 1, price = 1000)),
            result
        )
    }

    @Test
    @DisplayName("Empty seat list returns empty response")
    fun emptySeats() = runBlocking {
        val repository = mock<SeatPriceRepository>()
        val seatService = mock<SeatService>()
        val service = SeatPriceService(repository, seatService)

        whenever(repository.findSeatPricesByShowAndSeatIds(1L, emptyList())).thenReturn(emptyList())

        val result = service.findByShowAndSeatIds(1L, emptyList())

        assertTrue(result.isEmpty())
        verifyNoInteractions(seatService)
    }

    @Test
    @DisplayName("Missing seats are skipped in response")
    fun missingSeats() = runBlocking {
        val repository = mock<SeatPriceRepository>()
        val seatService = mock<SeatService>()
        val service = SeatPriceService(repository, seatService)
        val prices = listOf(
            SeatPrice(seatId = 1L, showId = 1L, price = 1000),
            SeatPrice(seatId = 2L, showId = 1L, price = 1200)
        )
        val seats = listOf(
            Seat(id = 1L, rowNumber = 1, seatNumber = 1, hallId = 1L)
        )

        whenever(repository.findSeatPricesByShowAndSeatIds(1L, listOf(1L, 2L))).thenReturn(prices)
        whenever(seatService.findAllByIds(listOf(1L, 2L))).thenReturn(seats)

        val result = service.findByShowAndSeatIds(1L, listOf(1L, 2L))

        assertEquals(
            listOf(SeatPriceDto(id = 1, raw = 1, number = 1, price = 1000)),
            result
        )
    }
}
