package com.flourenco.fibonacci.model

import java.math.BigInteger
import org.threeten.bp.LocalDateTime

data class FibonacciEntry(
    val orderNumber: Int,
    val fibonacciValue: BigInteger,
    val time: LocalDateTime
)
