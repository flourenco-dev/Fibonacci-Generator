package com.flourenco.fibonacci.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flourenco.fibonacci.core.Repository
import com.flourenco.fibonacci.exception.AddFibonacciToDatabaseException
import com.flourenco.fibonacci.model.FibonacciEntry
import com.flourenco.fibonacci.model.RequestNewFibonacciResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class FibonacciViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    fun getFibonacciListObservable(): Flow<List<FibonacciEntry>> = repository.getFibonacciEntries()

    // Returning LiveData just as an alternative
    fun requestNewFibonacci(): LiveData<RequestNewFibonacciResult> {
        val newFibonacciResultObservable = MutableLiveData<RequestNewFibonacciResult>()
        viewModelScope.launch {
            try {
                repository.requestNewFibonacci()
                newFibonacciResultObservable.postValue(RequestNewFibonacciResult.Success)
            } catch (error: AddFibonacciToDatabaseException) {
                newFibonacciResultObservable.postValue(RequestNewFibonacciResult.Failure)
            } catch (error: Exception) {
                // Unexpected error happened so we are preventing the crash, informing the user and
                // logging the crash for debug purposes
                Timber.w(error)
                newFibonacciResultObservable.postValue(RequestNewFibonacciResult.UnknownError)
            }
        }
        return newFibonacciResultObservable
    }
}