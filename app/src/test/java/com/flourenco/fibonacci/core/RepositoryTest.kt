package com.flourenco.fibonacci.core

import com.flourenco.fibonacci.core.calculator.FibonacciCalculator
import com.flourenco.fibonacci.core.dateTime.DateTimeHelper
import com.flourenco.fibonacci.core.storage.StorageHelper
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntryEntity
import com.flourenco.fibonacci.exception.AddFibonacciToDatabaseException
import com.flourenco.fibonacci.model.FibonacciEntry
import java.math.BigInteger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.given
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryTest {

    private val fakeStorageHelper: StorageHelper = mock(StorageHelper::class.java)
    private val fakeFibonacciCalculator: FibonacciCalculator = mock(FibonacciCalculator::class.java)
    private val fakeDateTimeHelper: DateTimeHelper = mock(DateTimeHelper::class.java)
    private val repository = RepositoryImpl(
        storageHelper = fakeStorageHelper,
        fibonacciCalculator = fakeFibonacciCalculator,
        dateTimeHelper = fakeDateTimeHelper
    )

    @Test
    fun `getFibonacciEntries() returns list of correctly sorted Fibonacci entries`() {
        runTest {
            val orderNumber1 = 8
            val fibonacciValue1 = BigInteger("21")
            val dateTime1 = LocalDateTime.now()
            val orderNumber2 = 2
            val fibonacciValue2 = BigInteger("1")
            val dateTime2 = LocalDateTime.now().minusHours(1)
            val orderNumber3 = 5
            val fibonacciValue3 = BigInteger("5")
            val dateTime3 = LocalDateTime.now().plusHours(1)
            val fibonacciEntryEntitiesList = listOf(
                FibonacciEntryEntity(
                    number = orderNumber1,
                    fibonacciValue = fibonacciValue1,
                    entryTime = dateTime1
                ),
                FibonacciEntryEntity(
                    number = orderNumber2,
                    fibonacciValue = fibonacciValue2,
                    entryTime = dateTime2
                ),
                FibonacciEntryEntity(
                    number = orderNumber3,
                    fibonacciValue = fibonacciValue3,
                    entryTime = dateTime3
                )
            )
            val expectedFibonacciEntries = listOf(
                FibonacciEntry(
                    orderNumber = orderNumber3,
                    fibonacciValue = fibonacciValue3,
                    time = dateTime3
                ),
                FibonacciEntry(
                    orderNumber = orderNumber1,
                    fibonacciValue = fibonacciValue1,
                    time = dateTime1
                ),
                FibonacciEntry(
                    orderNumber = orderNumber2,
                    fibonacciValue = fibonacciValue2,
                    time = dateTime2
                )
            )
            given(fakeStorageHelper.getFibonacciEntries())
                .willReturn(MutableStateFlow(fibonacciEntryEntitiesList))

            val fibonacciEntriesFlow = repository.getFibonacciEntries()

            assertEquals(expectedFibonacciEntries, fibonacciEntriesFlow.firstOrNull())
        }
    }

    @Test
    fun `Requesting new Fibonacci adds it to storage`() {
        runTest {
            val orderNumber = 8
            val fibonacciValue = BigInteger("21")
            val dateTime = LocalDateTime.now()
            val fibonacciEntryEntity = FibonacciEntryEntity(
                orderNumber,
                fibonacciValue,
                dateTime
            )
            given(fakeFibonacciCalculator.getRandomOrderNumber()).willReturn(orderNumber)
            given(fakeFibonacciCalculator.getFibonacciValueForNumberAsync(orderNumber))
                .willReturn(CompletableDeferred(fibonacciValue))
            given(fakeDateTimeHelper.getCurrentDateTime()).willReturn(dateTime)
            given(fakeStorageHelper.addFibonacciEntry(fibonacciEntryEntity)).willReturn(true)

            repository.requestNewFibonacci()

            verify(fakeStorageHelper).addFibonacciEntry(fibonacciEntryEntity)
        }
    }

    @Test
    fun `Requesting new Fibonacci and failing to add it to storage leads to exception`() {
        runTest {
            val orderNumber = 8
            val fibonacciValue = BigInteger("21")
            val dateTime = LocalDateTime.now()
            val fibonacciEntryEntity = FibonacciEntryEntity(
                orderNumber,
                fibonacciValue,
                dateTime
            )
            given(fakeFibonacciCalculator.getRandomOrderNumber()).willReturn(orderNumber)
            given(fakeFibonacciCalculator.getFibonacciValueForNumberAsync(orderNumber))
                .willReturn(CompletableDeferred(fibonacciValue))
            given(fakeDateTimeHelper.getCurrentDateTime()).willReturn(dateTime)
            given(fakeStorageHelper.addFibonacciEntry(fibonacciEntryEntity)).willReturn(false)
            var wasAddFibonacciToDatabaseExceptionThrown = false

            try {
                repository.requestNewFibonacci()
            } catch (error: AddFibonacciToDatabaseException) {
                wasAddFibonacciToDatabaseExceptionThrown = true
            }

            verify(fakeStorageHelper).addFibonacciEntry(fibonacciEntryEntity)
            assertTrue(wasAddFibonacciToDatabaseExceptionThrown)
        }
    }
}