package com.flourenco.fibonacci.model

sealed class RequestNewFibonacciResult {
    // Using sealed class because in a real app Success would probably be a data class with the
    // operation result value as property
    object Success: RequestNewFibonacciResult()
    object Failure: RequestNewFibonacciResult()
    object UnknownError: RequestNewFibonacciResult()
}
