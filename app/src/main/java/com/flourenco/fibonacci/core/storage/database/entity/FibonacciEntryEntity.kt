package com.flourenco.fibonacci.core.storage.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigInteger
import org.threeten.bp.LocalDateTime

@Entity
data class FibonacciEntryEntity(
    @PrimaryKey val number: Int,
    val fibonacciValue: BigInteger,
    val entryTime: LocalDateTime
)
