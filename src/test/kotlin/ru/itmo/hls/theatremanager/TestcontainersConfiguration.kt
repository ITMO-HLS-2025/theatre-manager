package ru.itmo.hls.theatremanager

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import javax.sql.DataSource

@TestConfiguration(proxyBeanMethods = false)
open class TestcontainersConfiguration : PostgresContainerConfig() {
    @Bean
    fun dataSource(): DataSource {
        val container = postgresContainer()
        return DriverManagerDataSource().apply {
            setDriverClassName("org.postgresql.Driver")
            url = container.jdbcUrl
            username = container.username
            password = container.password
        }
    }

    @Bean
    fun r2dbcTransactionManager(connectionFactory: ConnectionFactory): R2dbcTransactionManager =
        R2dbcTransactionManager(connectionFactory)
}
