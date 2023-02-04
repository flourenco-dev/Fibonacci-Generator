package com.flourenco.fibonacci.core.storage

import com.flourenco.fibonacci.core.storage.database.entity.CalculatedFibonacciEntity
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntryEntity
import kotlinx.coroutines.flow.Flow

interface StorageHelper {
    fun getFibonacciEntries(): Flow<List<FibonacciEntryEntity>>
    suspend fun addFibonacciEntry(entry: FibonacciEntryEntity): Boolean
    suspend fun addCalculatedFibonacci(calculatedFibonacci: CalculatedFibonacciEntity)
    suspend fun hasCalculatedFibonacciForNumber(number: Int): Boolean
    suspend fun getCalculatedFibonacciForNumber(number: Int): CalculatedFibonacciEntity
}