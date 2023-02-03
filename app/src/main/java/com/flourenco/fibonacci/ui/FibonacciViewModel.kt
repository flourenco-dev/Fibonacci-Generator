package com.flourenco.fibonacci.ui

import androidx.lifecycle.ViewModel
import com.flourenco.fibonacci.core.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FibonacciViewModel @Inject constructor(private val repository: Repository): ViewModel() {

}