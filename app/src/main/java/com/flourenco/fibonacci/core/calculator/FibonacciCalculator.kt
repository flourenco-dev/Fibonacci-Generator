package com.flourenco.fibonacci.core.calculator

import java.math.BigInteger
import kotlinx.coroutines.Deferred

interface FibonacciCalculator {
    fun getRandomOrderNumber(): Int
    suspend fun getFibonacciValueForNumberAsync(number: Int): Deferred<BigInteger>
}