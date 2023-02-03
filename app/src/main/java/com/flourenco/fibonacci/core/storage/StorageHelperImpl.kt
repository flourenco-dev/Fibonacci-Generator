package com.flourenco.fibonacci.core.storage

import com.flourenco.fibonacci.core.storage.database.dao.FibonacciDao
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class StorageHelperImpl @Inject constructor(
    private val fibonacciDao: FibonacciDao
): StorageHelper {
    override fun getFibonacciEntries(): Flow<List<FibonacciEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun addFibonacciEntry(entry: FibonacciEntity): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFibonacciEntryByNumber(number: Int): FibonacciEntity? {
        TODO("Not yet implemented")
    }
}