package ru.itmo.hls.theatremanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class TheatreServiceApplication

fun main(args: Array<String>) {
    runApplication<TheatreServiceApplication>(*args)
}
