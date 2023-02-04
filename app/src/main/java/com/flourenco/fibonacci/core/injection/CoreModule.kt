package com.flourenco.fibonacci.core.injection

import android.content.Context
import androidx.room.Room
import com.flourenco.fibonacci.core.Repository
import com.flourenco.fibonacci.core.RepositoryImpl
import com.flourenco.fibonacci.core.storage.StorageHelper
import com.flourenco.fibonacci.core.storage.StorageHelperImpl
import com.flourenco.fibonacci.core.storage.database.FibonacciDatabase
import com.flourenco.fibonacci.core.storage.database.dao.CalculatedFibonacciDao
import com.flourenco.fibonacci.core.storage.database.dao.FibonacciEntryDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun providesRepository(repositoryImpl: RepositoryImpl): Repository
}

@InstallIn(SingletonComponent::class)
@Module
abstract class StorageModule {
    @Binds
    abstract fun providesStorage(storageHelperImpl: StorageHelperImpl): StorageHelper
}

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun providesFibonacciEntryDao(fibonacciDatabase: FibonacciDatabase): FibonacciEntryDao =
        fibonacciDatabase.fibonacciEntryDao

    @Provides
    fun providesCalculatedFibonacciDao(
        fibonacciDatabase: FibonacciDatabase
    ): CalculatedFibonacciDao = fibonacciDatabase.calculatedFibonacciDao

    @Provides
    @Singleton
    fun providesFibonacciDatabase(@ApplicationContext context: Context): FibonacciDatabase =
        Room.databaseBuilder(
            context = context,
            klass = FibonacciDatabase::class.java,
            name = "Fibonacci App"
        ).build()
}

