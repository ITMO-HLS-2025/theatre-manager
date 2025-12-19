package ru.itmo.hls.theatreservice.entity

import jakarta.persistence.*


@Entity
@Table(name = "theatre")
class Theatre(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    val name: String,
    val city: String,
    val address: String,
    @OneToMany(mappedBy = "theatre", cascade = [CascadeType.ALL], orphanRemoval = true)
    var halls: MutableList<Hall> = mutableListOf(),
)
{
    constructor() : this(name = "", city = "", address = "")
}