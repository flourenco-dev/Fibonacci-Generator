package com.flourenco.fibonacci.core

import com.flourenco.fibonacci.core.storage.StorageHelper
import com.flourenco.fibonacci.core.storage.database.entity.CalculatedFibonacciEntity
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntryEntity
import com.flourenco.fibonacci.exception.AddFibonacciToDatabaseException
import com.flourenco.fibonacci.model.FibonacciEntry
import java.math.BigInteger
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

    // Limiting the order number to 250 to avoid to big Fibonacci results
    // Feel free to increase max order number, i.e to 10000, to test performance be aware that the
    // Fibonacci result maybe bigger than the visible cell.
    // To make the app have a better look reduce the limit to 75
    private fun getRandomInt(): Int = (0..250).random()

    private suspend fun getFibonacciValueForNumberAsync(number: Int): Deferred<BigInteger> {
        Timber.d("Fibonacci for $number")
        val deferredFibonacciValue = CompletableDeferred<BigInteger>()
        withContext(Dispatchers.IO) {
            val fibonacciValue = when {
                number == 0 -> BigInteger("0")
                number == 1 -> BigInteger("1")
                storageHelper.hasCalculatedFibonacciForNumber(number) -> {
                    storageHelper.getCalculatedFibonacciForNumber(number).fibonacciValue
                }
                else -> {
                    (getFibonacciValueForNumberAsync(number - 1).await()
                        .plus(getFibonacciValueForNumberAsync(number - 2).await())).also {
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