package com.flourenco.fibonacci.core.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flourenco.fibonacci.core.storage.database.converters.LocalDateTimeConverter
import com.flourenco.fibonacci.core.storage.database.dao.FibonacciDao
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntity

@Database(
    entities = [FibonacciEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalDateTimeConverter::class
)
abstract class FibonacciDatabase : RoomDatabase() {
    abstract val fibonacciDao: FibonacciDao
}