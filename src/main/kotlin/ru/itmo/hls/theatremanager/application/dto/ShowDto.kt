package ru.itmo.hls.theatremanager.application.dto

import java.time.LocalDateTime

data class ShowViewDto(
    val id: Long,
    val title: String,
    val date: LocalDateTime,
)
