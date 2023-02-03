package com.flourenco.fibonacci.core.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FibonacciDao {

    @Query("SELECT * FROM FibonacciEntity")
    abstract fun getEntries(): Flow<List<FibonacciEntity>>

    @Query("SELECT 1 FROM FibonacciEntity WHERE number = :entryNumber")
    abstract suspend fun contains(entryNumber: String): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entry: FibonacciEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun update(entry: FibonacciEntity): Int

    @Transaction
    open suspend fun insertOrUpdate(entry: FibonacciEntity): Boolean {
        return insert(entry) > -1 || update(entry) > -1
    }
}