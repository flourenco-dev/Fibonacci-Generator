package com.flourenco.fibonacci.core.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.flourenco.fibonacci.core.storage.database.entity.FibonacciEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FibonacciEntryDao {

    @Query("SELECT * FROM FibonacciEntryEntity")
    abstract fun getEntries(): Flow<List<FibonacciEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entry: FibonacciEntryEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun update(entry: FibonacciEntryEntity): Int

    @Transaction
    open suspend fun insertOrUpdate(entry: FibonacciEntryEntity): Boolean {
        return insert(entry) > -1 || update(entry) > -1
    }
}