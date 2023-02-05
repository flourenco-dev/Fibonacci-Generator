package com.flourenco.fibonacci.core

import com.flourenco.fibonacci.core.calculator.FibonacciCalculator
import com.flourenco.fibonacci.core.dateTime.DateTimeHelper
import com.flourenco.fibonacci.core.storage.StorageHelper
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntryEntity
import com.flourenco.fibonacci.exception.AddFibonacciToDatabaseException
import com.flourenco.fibonacci.model.FibonacciEntry
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryImpl @Inject constructor(
    private val storageHelper: StorageHelper,
    private val fibonacciCalculator: FibonacciCalculator,
    private val dateTimeHelper: DateTimeHelper
): Repository {

    override fun getFibonacciEntries(): Flow<List<FibonacciEntry>> =
        storageHelper.getFibonacciEntries().map {
            it.map { entry ->
                entry.toFibonacciEntry()
            }.sortedByDescending {  fibonacci ->
                fibonacci.time
            }
        }

    private fun FibonacciEntryEntity.toFibonacciEntry(): FibonacciEntry =
        FibonacciEntry(
            orderNumber = number,
            fibonacciValue = fibonacciValue,
            time = entryTime
        )

    override suspend fun requestNewFibonacci() {
        val number = fibonacciCalculator.getRandomOrderNumber()
        val fibonacciValue = fibonacciCalculator.getFibonacciValueForNumberAsync(number).await()
        // Add entry to storage to keep track of previous and current requested Fibonacci values
        val isAddSuccessful = storageHelper.addFibonacciEntry(
            FibonacciEntryEntity(
                number = number,
                fibonacciValue = fibonacciValue,
                entryTime = dateTimeHelper.getCurrentDateTime()
            )
        )
        if (isAddSuccessful.not()) {
            throw AddFibonacciToDatabaseException()
        }
    }
}