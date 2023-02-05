package com.flourenco.fibonacci.core.storage.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity
data class FibonacciEntryEntity(
    @PrimaryKey val number: Int,
    val fibonacciValue: Long,
    val entryTime: LocalDateTime
)
