package com.flourenco.fibonacci.ui.fibonacciList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.flourenco.fibonacci.R
import com.flourenco.fibonacci.model.FibonacciEntry
import com.flourenco.fibonacci.model.RequestNewFibonacciResult
import com.flourenco.fibonacci.ui.FibonacciViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FibonacciList(
    fibonacciViewModel: FibonacciViewModel,
    fibonacciEntryListState: State<List<FibonacciEntry>>
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val showLoading = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.fibonacci_list_title))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    showLoading.value = true
                    fibonacciViewModel.requestNewFibonacci().observe(lifecycleOwner) {
                        showLoading.value = false
                        scope.launch {
                            when (it) {
                                RequestNewFibonacciResult.Success -> {
                                    // Success will lead to a list update so no need to bother users
                                    // with messages
                                }
                                RequestNewFibonacciResult.Failure -> {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(
                                            R.string.fibonacci_list_error_failure_message
                                        )
                                    )
                                }
                                RequestNewFibonacciResult.UnknownError -> {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(
                                            R.string.fibonacci_list_error_unknown_error_message
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add button"
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { contentPadding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            val (loading,  fibonacciList, emptyView) = createRefs()
            if (showLoading.value) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(loading) {
                            top.linkTo(parent.top)
                        }
                )
            }
            if (fibonacciEntryListState.value.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .constrainAs(emptyView) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    text = stringResource(R.string.fibonacci_list_empty_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().constrainAs(fibonacciList) {
                        top.linkTo(parent.top)
                    }) {
                    itemsIndexed(items = fibonacciEntryListState.value) { index, item ->
                        if (index == 0) {
                            FirstFibonacciItem(fibonacciEntry = item)
                        } else {
                            FibonacciItem(fibonacciEntry = item)
                        }
                    }
                }
            }
        }
    }
}