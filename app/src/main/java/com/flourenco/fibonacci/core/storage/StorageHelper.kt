package com.flourenco.fibonacci.core.storage

import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntity
import kotlinx.coroutines.flow.Flow

interface StorageHelper {
    fun getFibonacciEntries(): Flow<List<FibonacciEntity>>
    suspend fun addFibonacciEntry(entry: FibonacciEntity): Boolean
    suspend fun getFibonacciEntryByNumber(number: Int): FibonacciEntity?
}