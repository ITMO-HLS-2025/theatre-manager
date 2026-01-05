package ru.itmo.hls.theatremanager.validation

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PaginationValidatorTest {

    @Test
    @DisplayName("validateSize accepts values within bounds")
    fun validateSizeOk() {
        val validator = PaginationValidator(5)
        assertDoesNotThrow { validator.validateSize(1) }
        assertDoesNotThrow { validator.validateSize(5) }
    }

    @Test
    @DisplayName("validateSize rejects values outside bounds")
    fun validateSizeFails() {
        val validator = PaginationValidator(5)
        assertThrows(IllegalArgumentException::class.java) { validator.validateSize(0) }
        assertThrows(IllegalArgumentException::class.java) { validator.validateSize(6) }
    }
}
