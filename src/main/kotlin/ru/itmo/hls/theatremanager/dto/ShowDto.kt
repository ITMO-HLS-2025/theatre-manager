package ru.itmo.hls.theatremanager.dto

import java.time.LocalDateTime

data class ShowViewDto(
    val id: Long,
    val tittle: String,
    val date: LocalDateTime,
)
