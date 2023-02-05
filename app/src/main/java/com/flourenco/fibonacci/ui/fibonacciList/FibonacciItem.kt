package com.flourenco.fibonacci.ui.fibonacciList

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.flourenco.fibonacci.model.FibonacciEntry
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@Composable
fun FibonacciItem(fibonacciEntry: FibonacciEntry) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (dateTime, orderNumber, fibonacciValue) = createRefs()
            Text(
                modifier = Modifier.constrainAs(dateTime) {
                    top.linkTo(parent.top, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                },
                text = fibonacciEntry.time.format(
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Text(
                modifier = Modifier.constrainAs(orderNumber) {
                    top.linkTo(dateTime.bottom)
                    start.linkTo(parent.start, 16.dp)
                },
                text = "Fibonacci of order ${fibonacciEntry.orderNumber}:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.constrainAs(fibonacciValue) {
                    top.linkTo(orderNumber.bottom,8.dp)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                },
                text = "${fibonacciEntry.fibonacciValue}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}