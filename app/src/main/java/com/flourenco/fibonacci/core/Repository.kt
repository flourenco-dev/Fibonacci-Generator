package com.flourenco.fibonacci.core

import com.flourenco.fibonacci.model.FibonacciEntry
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getFibonacciEntries(): Flow<List<FibonacciEntry>>
    suspend fun requestNewFibonacci()
}