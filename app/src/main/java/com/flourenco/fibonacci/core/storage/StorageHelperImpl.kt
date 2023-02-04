package com.flourenco.fibonacci.core.storage

import com.flourenco.fibonacci.core.storage.database.dao.CalculatedFibonacciDao
import com.flourenco.fibonacci.core.storage.database.dao.FibonacciEntryDao
import com.flourenco.fibonacci.core.storage.database.entity.CalculatedFibonacciEntity
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntryEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class StorageHelperImpl @Inject constructor(
    private val fibonacciEntryDao: FibonacciEntryDao,
    private val calculatedFibonacciDao: CalculatedFibonacciDao
): StorageHelper {
    override fun getFibonacciEntries(): Flow<List<FibonacciEntryEntity>> = fibonacciEntryDao.getEntries()

    override suspend fun addFibonacciEntry(entry: FibonacciEntryEntity): Boolean =
        fibonacciEntryDao.insertOrUpdate(entry)

    override suspend fun addCalculatedFibonacci(calculatedFibonacci: CalculatedFibonacciEntity) {
        calculatedFibonacciDao.insert(calculatedFibonacci)
    }

    override suspend fun hasCalculatedFibonacciForNumber(number: Int): Boolean =
        calculatedFibonacciDao.contains(calculatedNumber = number)

    override suspend fun getCalculatedFibonacciForNumber(number: Int): CalculatedFibonacciEntity =
        calculatedFibonacciDao.getByNumber(calculatedNumber = number)
}