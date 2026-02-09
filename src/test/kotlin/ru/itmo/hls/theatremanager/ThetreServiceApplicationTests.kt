package ru.itmo.hls.theatremanager

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import ru.itmo.hls.theatremanager.testconfig.PostgresContainerConfig
import org.springframework.test.annotation.DirtiesContext

@Import(TestcontainersConfiguration::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TheatreServiceApplicationTests : PostgresContainerConfig() {

    @Test
    fun contextLoads() {
    }

}
