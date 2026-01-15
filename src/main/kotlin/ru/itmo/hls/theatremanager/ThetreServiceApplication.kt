package ru.itmo.hls.theatremanager

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@OpenAPIDefinition(info = Info(title = "Theatre Manager API", version = "v1"))
@SpringBootApplication
class TheatreServiceApplication

fun main(args: Array<String>) {
    runApplication<TheatreServiceApplication>(*args)
}
