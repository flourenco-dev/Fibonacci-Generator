package com.flourenco.fibonacci.core.storage.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity
data class FibonacciEntity(
    @PrimaryKey val number: Int,
    val entryTime: LocalDateTime
)
