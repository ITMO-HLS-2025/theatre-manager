package ru.itmo.hls.theatremanager.validation

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PaginationValidator(
    @Value("\${spring.application.pagination.max-size:50}")
    private val maxSize: Int
) {
    fun validateSize(size: Int) {
        require(size in 1..maxSize) { "Page size must be between 1 and $maxSize" }
    }
}
