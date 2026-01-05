package ru.itmo.hls.theatremanager.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.itmo.hls.theatremanager.TestcontainersConfiguration
import ru.itmo.hls.theatremanager.client.ShowClient
import ru.itmo.hls.theatremanager.dto.HallCreateDto
import ru.itmo.hls.theatremanager.dto.HallCreatePayload
import ru.itmo.hls.theatremanager.dto.HallDto
import ru.itmo.hls.theatremanager.dto.HallViewDto
import ru.itmo.hls.theatremanager.dto.SeatRawDto
import ru.itmo.hls.theatremanager.dto.SeatStatus
import ru.itmo.hls.theatremanager.dto.SeatStatusDto
import ru.itmo.hls.theatremanager.dto.TheatreCreatePayload
import ru.itmo.hls.theatremanager.dto.TheatrePayload

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration::class)
class TheatreControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var showClient: ShowClient

    @BeforeEach
    fun setUp() {
        whenever(showClient.findAllByTheatreId(any(), any(), any())).thenReturn(emptyList())
    }

    @Test
    @DisplayName("GET /api/theatres returns list with pagination headers")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun getAllTheatres() {
        val result = mockMvc.perform(
            get("/api/theatres")
                .queryParam("city", "Moscow")
                .queryParam("page", "1")
                .queryParam("size", "10")
        )
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(header().string("X-Total-Count", "2"))
            .andExpect(header().string("X-Page-Number", "1"))
            .andExpect(header().string("X-Page-Size", "10"))
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].city", `is`("Moscow")))
    }

    @Test
    @DisplayName("GET /api/theatres/{id} returns theatre details")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun getTheatreInfo() {
        val result = mockMvc.perform(get("/api/theatres/1"))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name", `is`("Mariinsky Theatre")))
            .andExpect(jsonPath("$.halls", hasSize<Any>(2)))
    }

    @Test
    @DisplayName("POST /api/theatres creates theatre")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun createTheatre() {
        val payload = TheatreCreatePayload(
            name = "New Theatre",
            city = "Moscow",
            address = "New Street 1",
            halls = listOf(
                HallCreatePayload(number = 1),
                HallCreatePayload(number = 2)
            )
        )

        val result = mockMvc.perform(
            post("/api/theatres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload))
        )
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name", `is`("New Theatre")))
            .andExpect(jsonPath("$.halls", hasSize<Any>(2)))
    }

    @Test
    @DisplayName("PUT /api/theatres updates theatre")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun updateTheatre() {
        val payload = TheatrePayload(
            id = 1,
            name = "Updated Theatre",
            city = "Moscow",
            address = "Updated Street 1",
            halls = listOf(
                HallViewDto(id = 1, number = 11),
                HallViewDto(id = 2, number = 12)
            )
        )

        val result = mockMvc.perform(
            put("/api/theatres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload))
        )
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name", `is`("Updated Theatre")))
            .andExpect(jsonPath("$.halls[0].number", `is`(11)))
    }

    @Test
    @DisplayName("POST /api/theatres/{id}/halls creates hall with seats")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun createHall() {
        val seatRows = listOf(
            SeatRawDto(
                row = 1,
                seats = listOf(
                    SeatStatusDto(id = 0L, status = SeatStatus.FREE, number = 1, price = 1000)
                )
            )
        )
        val payload = HallCreateDto(number = 5, seatRows = seatRows)

        val result = mockMvc.perform(
            post("/api/theatres/1/halls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload))
        )
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.number", `is`(5)))
            .andExpect(jsonPath("$.seatRows", hasSize<Any>(1)))
    }

    @Test
    @DisplayName("PUT /api/theatres/halls/{id} updates hall")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun updateHall() {
        val seatRows = listOf(
            SeatRawDto(
                row = 1,
                seats = listOf(
                    SeatStatusDto(id = 0L, status = SeatStatus.FREE, number = 1, price = 1000)
                )
            )
        )
        val payload = HallDto(id = null, number = 9, seatRows = seatRows)

        val result = mockMvc.perform(
            put("/api/theatres/halls/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload))
        )
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.number", `is`(9)))
    }

    @Test
    @DisplayName("DELETE /api/theatres/{id} deletes theatre")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun deleteTheatre() {
        val result = mockMvc.perform(delete("/api/theatres/1"))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("DELETE /api/theatres/halls/{id} deletes hall")
    @Sql(
        scripts = ["classpath:sql/clean.sql", "classpath:sql/init.sql", "classpath:sql/insert.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun deleteHall() {
        val result = mockMvc.perform(delete("/api/theatres/halls/1"))
            .andExpect(request().asyncStarted())
            .andReturn()

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk)
    }
}
