package ru.itmo.hls.theatremanager.service

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.jdbc.Sql
import ru.itmo.hls.theatremanager.client.ShowClient
import ru.itmo.hls.theatremanager.dto.HallCreateDto
import ru.itmo.hls.theatremanager.dto.HallCreatePayload
import ru.itmo.hls.theatremanager.dto.HallDto
import ru.itmo.hls.theatremanager.dto.HallViewDto
import ru.itmo.hls.theatremanager.dto.SeatCreateDto
import ru.itmo.hls.theatremanager.dto.SeatRowCreateDto
import ru.itmo.hls.theatremanager.dto.TheatreCreatePayload
import ru.itmo.hls.theatremanager.dto.TheatrePayload
import ru.itmo.hls.theatremanager.exception.HallNotFoundException
import ru.itmo.hls.theatremanager.exception.TheatreNotFoundException
import org.junit.jupiter.api.BeforeEach
import ru.itmo.hls.theatremanager.PostgresContainerConfig
import ru.itmo.hls.theatremanager.TestcontainersConfiguration

@SpringBootTest
@Import(TestcontainersConfiguration::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TheatreServiceIntegrationTest : PostgresContainerConfig() {

    @Autowired
    private lateinit var theatreService: TheatreService

    @MockitoBean
    private lateinit var showClient: ShowClient

    @BeforeEach
    fun setUp() {
        whenever(showClient.findAllByTheatreId(any(), any(), any())).thenReturn(emptyList())
    }

    @Test
    @DisplayName("Create theatre with halls")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun createTheatreWithHalls() = runBlocking {
        val halls = listOf(
            HallCreatePayload(number = 1),
            HallCreatePayload(number = 2)
        )

        val payload = TheatreCreatePayload(
            name = "New Theatre",
            city = "Saint Petersburg",
            address = "Nevsky Avenue 1",
            halls = halls
        )

        val created = theatreService.createTheatre(payload)

        assertNotNull(created.id)
        assertEquals(2, created.halls.size)
        assertEquals("New Theatre", created.name)
    }

    @Test
    @DisplayName("Update theatre throws when id = 0")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun updateTheatreThrowsWhenIdNull() {
        val payload = TheatrePayload(
            id = 0,
            name = "Theatre without id",
            city = "Moscow",
            address = "Red Square 1",
            halls = emptyList()
        )

        assertThrows<TheatreNotFoundException> {
            runBlocking { theatreService.updateTheatre(payload) }
        }
    }

    @Test
    @DisplayName("Update theatre throws when id not found")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun updateTheatreThrowsWhenIdNotFound() {
        val payload = TheatrePayload(
            id = 999L,
            name = "Missing Theatre",
            city = "Moscow",
            address = "Unknown Street 1",
            halls = emptyList()
        )

        assertThrows<TheatreNotFoundException> {
            runBlocking { theatreService.updateTheatre(payload) }
        }
    }

    @Test
    @DisplayName("Update theatre success")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun updateTheatreSuccess() = runBlocking {
        val updatePayload = TheatrePayload(
            id = 1L,
            name = "Updated Theatre",
            city = "Moscow",
            address = "New Street 5",
            halls = listOf(
                HallViewDto(id = 1L, number = 10, theatreId = 1L),
                HallViewDto(id = 2L, number = 20, theatreId = 1L)
            )
        )

        val updated = theatreService.updateTheatre(updatePayload)

        assertEquals(1L, updated.id)
        assertEquals("Updated Theatre", updated.name)
        assertEquals("Moscow", updated.city)
        assertEquals(2, updated.halls.size)
        assertEquals(10, updated.halls[0].number)
    }

    @Test
    @DisplayName("Update theatre keeps halls when payload is empty")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun updateTheatreKeepsHallsWhenEmpty() = runBlocking {
        val updatePayload = TheatrePayload(
            id = 1L,
            name = "Updated Theatre",
            city = "Moscow",
            address = "New Street 5",
            halls = emptyList()
        )

        val updated = theatreService.updateTheatre(updatePayload)

        assertEquals(1L, updated.id)
        assertEquals(2, updated.halls.size)
    }

    @Test
    @DisplayName("Get theatre info success")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun getTheatreInfoSuccess() = runBlocking {
        val theatreId = 1L

        val theatre = theatreService.getTheatreInfo(theatreId)

        assertEquals("Mariinsky Theatre", theatre.name)
        assertTrue(theatre.halls.isNotEmpty())
    }

    @Test
    @DisplayName("Get theatre info throws when not found")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun getTheatreInfoThrowsWhenNotFound() {
        assertThrows<TheatreNotFoundException> {
            runBlocking { theatreService.getTheatreInfo(999L) }
        }
    }

    @Test
    @DisplayName("Get all theatres in city")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun getAllTheatresInCity() = runBlocking {
        val page = theatreService.getAllTheatreInCity("Moscow", 1, 10)

        assertTrue(page.content.isNotEmpty())
        assertEquals("Moscow", page.content.first().city)
    }

    @Test
    @DisplayName("Create hall with seat rows")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun createHallWithSeats() = runBlocking {
        val seatRows = listOf(
            SeatRowCreateDto(
                row = 1,
                seats = listOf(
                    SeatCreateDto(number = 1),
                    SeatCreateDto(number = 2)
                )
            )
        )

        val hallDto = HallCreateDto(number = 5, seatRows = seatRows)

        val created = theatreService.createHall(1L, hallDto)

        assertNotNull(created.id)
        assertEquals(5, created.number)
        assertEquals(1, created.seatRows.size)
        assertEquals(2, created.seatRows[0].seats.size)
    }

    @Test
    @DisplayName("Create hall throws when theatre not found")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun createHallThrowsWhenTheatreNotFound() {
        val seatRows = listOf(
            SeatRowCreateDto(
                row = 1,
                seats = listOf(
                    SeatCreateDto(number = 1)
                )
            )
        )
        val hallDto = HallCreateDto(number = 5, seatRows = seatRows)

        assertThrows<TheatreNotFoundException> {
            runBlocking { theatreService.createHall(999L, hallDto) }
        }
    }

    @Test
    @DisplayName("Delete theatre")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun deleteTheatre() {
        runBlocking { theatreService.deleteTheatre(1) }
        assertThrows<TheatreNotFoundException> {
            runBlocking { theatreService.getTheatreInfo(1) }
        }
    }

    @Test
    @DisplayName("Update hall with seat rows")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun updateHallWithSeats() = runBlocking {
        val seatRows = listOf(
            SeatRowCreateDto(
                row = 1,
                seats = listOf(
                    SeatCreateDto(number = 1)
                )
            )
        )

        val hallDto = HallDto(id = 1L, number = 10, seatRows = seatRows)

        val updated = theatreService.updateHall(hallDto)

        assertEquals(10, updated.number)
        assertEquals(1, updated.seatRows.size)
        assertEquals(1, updated.seatRows[0].seats.size)
    }

    @Test
    @DisplayName("Update hall throws when hall not found")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun updateHallThrowsWhenNotFound() {
        val seatRows = listOf(
            SeatRowCreateDto(
                row = 1,
                seats = listOf(
                    SeatCreateDto(number = 1)
                )
            )
        )
        val hallDto = HallDto(id = 999L, number = 10, seatRows = seatRows)

        assertThrows<HallNotFoundException> {
            runBlocking { theatreService.updateHall(hallDto) }
        }
    }

    @Test
    @DisplayName("Delete hall")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun deleteHall() {
        runBlocking { theatreService.deleteHall(1L) }
        assertThrows<HallNotFoundException> { runBlocking { theatreService.deleteHall(1L) } }
    }

    @Test
    @DisplayName("Delete hall not found")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun deleteHallNotFound() {
        assertThrows<HallNotFoundException> { runBlocking { theatreService.deleteHall(999L) } }
    }
}
