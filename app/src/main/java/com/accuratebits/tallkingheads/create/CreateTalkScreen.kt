package com.accuratebits.tallkingheads.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.accuratebits.tallkingheads.talks.TalksUiState
import com.accuratebits.tallkingheads.talks.TalksViewModel
import com.accuratebits.tallkingheads.ui.theme.TallkingHeadsTheme

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CreateTalkScreen(
    modifier: Modifier = Modifier,
    viewModel: TalksViewModel = hiltViewModel(),
    onNewClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is TalksUiState.Content -> CreateTalkScreen(
            preparedCount = state.prepared.count(),
            givenCount = state.given.count(),
            modifier = modifier,
            onNewClick = onNewClick
        )
        TalksUiState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CreateTalkScreen(
    preparedCount: Int,
    givenCount: Int,
    modifier: Modifier = Modifier,
    onNewClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = onNewClick) {
                Text("New talk", textAlign = TextAlign.Center)
            }

            Text(text = "My prepared talks: $preparedCount", style = MaterialTheme.typography.bodyLarge)
            Text(text = "My given talks: $givenCount", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateTalkScreenPreview() {
    TallkingHeadsTheme {
        CreateTalkScreen(3, 42, modifier = Modifier.fillMaxSize()) {}
    }
}