package ru.itmo.hls.theatremanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@SpringBootApplication
class ThetreServiceApplication

fun main(args: Array<String>) {
    runApplication<ThetreServiceApplication>(*args)
}
