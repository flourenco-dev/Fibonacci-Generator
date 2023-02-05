package com.flourenco.fibonacci.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.flourenco.fibonacci.ui.fibonacciList.FibonacciList
import com.flourenco.fibonacci.ui.theme.FibonacciTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val fibonacciViewModel by viewModels<FibonacciViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FibonacciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FibonacciList(
                        fibonacciViewModel = fibonacciViewModel,
                        fibonacciEntryListState = fibonacciViewModel.getFibonacciListObservable()
                            .collectAsState(initial = emptyList())
                    )
                }
            }
        }
    }
}