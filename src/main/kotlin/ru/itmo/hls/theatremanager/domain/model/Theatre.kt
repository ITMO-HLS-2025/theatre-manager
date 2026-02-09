package ru.itmo.hls.theatremanager.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("theatre")
data class Theatre(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("name")
    val name: String,

    @Column("city")
    val city: String,

    @Column("address")
    val address: String
)
