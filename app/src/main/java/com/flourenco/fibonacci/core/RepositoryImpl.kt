package com.flourenco.fibonacci.core

import com.flourenco.fibonacci.core.storage.StorageHelper
import com.flourenco.fibonacci.core.storage.database.entity.CalculatedFibonacciEntity
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntryEntity
import com.flourenco.fibonacci.exception.AddFibonacciToDatabaseException
import com.flourenco.fibonacci.model.FibonacciEntry
import javax.inject.Inject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class RepositoryImpl @Inject constructor(private val storageHelper: StorageHelper): Repository {

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
        val number = getRandomInt()
        val fibonacciValue = getFibonacciValueForNumberAsync(number).await()
        // Add entry to DB to keep track of previous and current requested Fibonacci values
        val isAddSuccessful = storageHelper.addFibonacciEntry(
            FibonacciEntryEntity(
                number = number,
                fibonacciValue = fibonacciValue,
                entryTime = LocalDateTime.now()
            )
        )
        if (isAddSuccessful.not()) {
            throw AddFibonacciToDatabaseException()
        }
    }

    // Limiting the order number to 75 to allow the Fibonacci result to be stored in a Long
    // Feel free to increase max order number, i.e to 10000, to test performance be aware that the
    // Fibonacci result maybe wrong due to not being able to represent it in a Long
    private fun getRandomInt(): Int = (0..75).random()

    private suspend fun getFibonacciValueForNumber(number: Int): Long =
        withContext(Dispatchers.IO) {
            Timber.d("Fibonacci for $number")
            when {
                number == 0 -> 0L
                number == 1 -> 1L
                storageHelper.hasCalculatedFibonacciForNumber(number) -> {
                    storageHelper.getCalculatedFibonacciForNumber(number).fibonacciValue
                }
                else -> {
                    (getFibonacciValueForNumber(number - 1) +
                            getFibonacciValueForNumber(number - 2)).also {
                        // Store calculated value to allow faster calculations
                        storageHelper.addCalculatedFibonacci(
                            CalculatedFibonacciEntity(
                                calculatedNumber = number,
                                fibonacciValue = it
                            )
                        )
                    }
                }
            }
        }

    private suspend fun getFibonacciValueForNumberAsync(number: Int): Deferred<Long> {
        Timber.d("Fibonacci for $number")
        val deferredFibonacciValue = CompletableDeferred<Long>()
        withContext(Dispatchers.IO) {
            val fibonacciValue = when {
                number == 0 -> 0L
                number == 1 -> 1L
                storageHelper.hasCalculatedFibonacciForNumber(number) -> {
                    storageHelper.getCalculatedFibonacciForNumber(number).fibonacciValue
                }
                else -> {
                    (getFibonacciValueForNumberAsync(number - 1).await() +
                            getFibonacciValueForNumberAsync(number - 2).await()).also {
                        // Store calculated value to allow faster calculations
                        storageHelper.addCalculatedFibonacci(
                            CalculatedFibonacciEntity(
                                calculatedNumber = number,
                                fibonacciValue = it
                            )
                        )
                    }
                }
            }
            deferredFibonacciValue.complete(fibonacciValue)
        }

        return deferredFibonacciValue
    }
}