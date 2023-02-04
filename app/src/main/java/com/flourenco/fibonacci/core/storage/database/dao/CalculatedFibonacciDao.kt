package com.flourenco.fibonacci.core.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flourenco.fibonacci.core.storage.database.entity.CalculatedFibonacciEntity

@Dao
abstract class CalculatedFibonacciDao {

    @Query("SELECT EXISTS (SELECT 1 FROM CalculatedFibonacciEntity WHERE calculatedNumber = :calculatedNumber)")
    abstract suspend fun contains(calculatedNumber: Int): Boolean

    @Query("SELECT * FROM CalculatedFibonacciEntity WHERE calculatedNumber = :calculatedNumber")
    abstract suspend fun getByNumber(calculatedNumber: Int): CalculatedFibonacciEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(calculatedFibonacci: CalculatedFibonacciEntity): Long
}