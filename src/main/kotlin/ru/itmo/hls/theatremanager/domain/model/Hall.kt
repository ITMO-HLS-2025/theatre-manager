package ru.itmo.hls.theatremanager.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("hall")
data class Hall(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("number")
    val number: Int,

    @Column("theatre_id")
    val theatreId: Long
)
