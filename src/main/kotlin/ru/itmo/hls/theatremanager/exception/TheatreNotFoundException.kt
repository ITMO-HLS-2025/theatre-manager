package ru.itmo.hls.theatremanager.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class TheatreNotFoundException(message: String) : RuntimeException(message)
