package com.zaripov.mypokedex.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokeListEntry constructor(
    @PrimaryKey
    val entryNum: Int,
    val name: String
)