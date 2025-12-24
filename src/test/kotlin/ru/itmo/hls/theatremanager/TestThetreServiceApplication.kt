package ru.itmo.hls.theatremanager

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<ThetreServiceApplication>().with(TestcontainersConfiguration::class).run(*args)
}
