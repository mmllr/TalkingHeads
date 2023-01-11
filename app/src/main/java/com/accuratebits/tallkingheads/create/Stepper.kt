package com.accuratebits.tallkingheads.create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun Stepper(
    value: Int,
    enabled: Boolean,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedRange<Int> = 0..Int.MAX_VALUE,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onValueChange(value.dec().coerceIn(valueRange)) },
                enabled = enabled && value > valueRange.start,
            ) { Text("-") }

            Divider(
                modifier = Modifier
                    .fillMaxHeight(0.75f)
                    .width(1.dp)
            )

            TextButton(
                onClick = { onValueChange(value.inc().coerceIn(valueRange)) },
                enabled = enabled && value < valueRange.endInclusive,
            ) { Text("+") }
        }
    }
}
