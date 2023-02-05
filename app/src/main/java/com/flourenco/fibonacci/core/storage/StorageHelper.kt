package com.flourenco.fibonacci.core.storage

import com.flourenco.fibonacci.core.storage.database.entity.CalculatedFibonacciEntity
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntryEntity
import kotlinx.coroutines.flow.Flow

// StorageHelper is an abstraction layer to deal with data persistence. For now we'll use Room as DB
// solution but it can be change to a different implementation and the repository would not need to
// be changed. This class can also contain an implementation of SharedPreferences
interface StorageHelper {
    fun getFibonacciEntries(): Flow<List<FibonacciEntryEntity>>
    suspend fun addFibonacciEntry(entry: FibonacciEntryEntity): Boolean
    suspend fun addCalculatedFibonacci(calculatedFibonacci: CalculatedFibonacciEntity)
    suspend fun hasCalculatedFibonacciForNumber(number: Int): Boolean
    suspend fun getCalculatedFibonacciForNumber(number: Int): CalculatedFibonacciEntity
}