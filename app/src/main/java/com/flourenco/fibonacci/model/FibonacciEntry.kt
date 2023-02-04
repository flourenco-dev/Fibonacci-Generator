package com.flourenco.fibonacci.model

import org.threeten.bp.LocalDateTime

data class FibonacciEntry(
    val orderNumber: Int,
    val fibonacciValue: Int,
    val time: LocalDateTime
)
