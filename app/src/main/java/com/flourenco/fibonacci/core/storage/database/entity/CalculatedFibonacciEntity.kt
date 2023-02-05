package com.flourenco.fibonacci.core.storage.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity
data class CalculatedFibonacciEntity(
    @PrimaryKey val calculatedNumber: Int,
    val fibonacciValue: Long
)
