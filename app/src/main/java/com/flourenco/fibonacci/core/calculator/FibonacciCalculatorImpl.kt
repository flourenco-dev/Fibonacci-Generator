package com.flourenco.fibonacci.core.calculator

import com.flourenco.fibonacci.core.storage.StorageHelper
import com.flourenco.fibonacci.core.storage.database.entity.CalculatedFibonacciEntity
import java.math.BigInteger
import javax.inject.Inject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class FibonacciCalculatorImpl @Inject constructor(
    private val storageHelper: StorageHelper
): FibonacciCalculator {

    // Limiting the order number to 250 to avoid to big Fibonacci results
    // Feel free to increase max order number, i.e to 10000, to test performance be aware that the
    // Fibonacci result maybe bigger than the visible cell.
    // To make the app have a better look reduce the limit to 75
    override fun getRandomOrderNumber(): Int = (0..250).random()

    override suspend fun getFibonacciValueForNumberAsync(number: Int): Deferred<BigInteger> {
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