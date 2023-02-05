package com.flourenco.fibonacci.core.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flourenco.fibonacci.core.storage.database.converters.BigIntegerConverter
import com.flourenco.fibonacci.core.storage.database.converters.LocalDateTimeConverter
import com.flourenco.fibonacci.core.storage.database.dao.CalculatedFibonacciDao
import com.flourenco.fibonacci.core.storage.database.dao.FibonacciEntryDao
import com.flourenco.fibonacci.core.storage.database.entity.CalculatedFibonacciEntity
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntryEntity

@Database(
    entities = [FibonacciEntryEntity::class, CalculatedFibonacciEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalDateTimeConverter::class,
    BigIntegerConverter::class
)
abstract class FibonacciDatabase : RoomDatabase() {
    abstract val fibonacciEntryDao: FibonacciEntryDao
    abstract val calculatedFibonacciDao: CalculatedFibonacciDao
}