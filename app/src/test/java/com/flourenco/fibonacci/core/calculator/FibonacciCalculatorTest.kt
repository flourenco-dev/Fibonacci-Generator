package com.flourenco.fibonacci.core.calculator

import com.flourenco.fibonacci.utils.anyObject
import com.flourenco.fibonacci.core.storage.StorageHelper
import com.flourenco.fibonacci.core.storage.database.entity.CalculatedFibonacciEntity
import java.math.BigInteger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.given

@OptIn(ExperimentalCoroutinesApi::class)
class FibonacciCalculatorTest {

    private val fakeStorageHelper: StorageHelper = mock(StorageHelper::class.java)
    private val fibonacciCalculator = FibonacciCalculatorImpl(storageHelper = fakeStorageHelper)

    @Test
    fun `Calculate Fibonacci order 0 should return 0`() {
        runTest {
            val orderNumber = 0
            val expectedFibonacciValue = BigInteger("0")

            val fibonacciValue = fibonacciCalculator.getFibonacciValueForNumberAsync(orderNumber)

            verify(fakeStorageHelper, never()).addCalculatedFibonacci(anyObject())
            assertEquals(expectedFibonacciValue, fibonacciValue.getCompleted())
        }
    }

    @Test
    fun `Calculate Fibonacci order 1 should return 1`() {
        runTest {
            val orderNumber = 1
            val expectedFibonacciValue = BigInteger("1")

            val fibonacciValue = fibonacciCalculator.getFibonacciValueForNumberAsync(orderNumber)

            verify(fakeStorageHelper, never()).addCalculatedFibonacci(anyObject())
            assertEquals(expectedFibonacciValue, fibonacciValue.getCompleted())
        }
    }

    @Test
    fun `Calculate Fibonacci order 8 should return 21`() {
        runTest {
            val orderNumber = 8
            val expectedFibonacciValue = BigInteger("21")
            given(fakeStorageHelper.hasCalculatedFibonacciForNumber(anyInt())).willReturn(false)
            given(fakeStorageHelper.addCalculatedFibonacci(anyObject())).willReturn(Unit)

            val fibonacciValue = fibonacciCalculator.getFibonacciValueForNumberAsync(orderNumber)

            assertEquals(expectedFibonacciValue, fibonacciValue.getCompleted())
        }
    }

    @Test
    fun `Calculate Fibonacci order 15 should return 610`() {
        runTest {
            val orderNumber = 15
            val expectedFibonacciValue = BigInteger("610")
            given(fakeStorageHelper.hasCalculatedFibonacciForNumber(anyInt())).willReturn(false)
            given(fakeStorageHelper.addCalculatedFibonacci(anyObject())).willReturn(Unit)

            val fibonacciValue = fibonacciCalculator.getFibonacciValueForNumberAsync(orderNumber)

            assertEquals(expectedFibonacciValue, fibonacciValue.getCompleted())
        }
    }

    // It should be called seven times due to the fact of using a recursive function and returning
    // false when checking if value already exists in storage
    @Test
    fun `Calculate Fibonacci order 5 should store seven values`() {
        runTest {
            val orderNumber = 5
            val expectedFibonacciValue = BigInteger("5")
            given(fakeStorageHelper.hasCalculatedFibonacciForNumber(anyInt())).willReturn(false)
            given(fakeStorageHelper.addCalculatedFibonacci(anyObject())).willReturn(Unit)

            val fibonacciValue = fibonacciCalculator.getFibonacciValueForNumberAsync(orderNumber)

            verify(fakeStorageHelper, times(7)).addCalculatedFibonacci(anyObject())
            assertEquals(expectedFibonacciValue, fibonacciValue.getCompleted())
        }
    }

    @Test
    fun `Calculate Fibonacci in storage should not add any values to storage`() {
        runTest {
            val orderNumber = 9
            val expectedFibonacciValue = BigInteger("34")
            val calculatedFibonacciEntity = CalculatedFibonacciEntity(
                calculatedNumber = orderNumber,
                fibonacciValue = expectedFibonacciValue
            )
            given(fakeStorageHelper.hasCalculatedFibonacciForNumber(orderNumber)).willReturn(true)
            given(fakeStorageHelper.getCalculatedFibonacciForNumber(orderNumber))
                .willReturn(calculatedFibonacciEntity)

            val fibonacciValue = fibonacciCalculator.getFibonacciValueForNumberAsync(orderNumber)

            verify(fakeStorageHelper, never()).addCalculatedFibonacci(anyObject())
            assertEquals(expectedFibonacciValue, fibonacciValue.getCompleted())
        }
    }
}