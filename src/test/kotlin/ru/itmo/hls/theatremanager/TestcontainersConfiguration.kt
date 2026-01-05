package ru.itmo.hls.theatremanager

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import io.r2dbc.spi.ConnectionFactory
import javax.sql.DataSource
import com.zaxxer.hikari.HikariDataSource

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
    }

    @Bean
    fun dataSource(container: PostgreSQLContainer<*>): DataSource {
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = container.jdbcUrl
        dataSource.username = container.username
        dataSource.password = container.password
        return dataSource
    }

    @Bean
    @Primary
    fun r2dbcTransactionManager(connectionFactory: ConnectionFactory): R2dbcTransactionManager =
        R2dbcTransactionManager(connectionFactory)

}
