package com.accuratebits.tallkingheads.talks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.accuratebits.tallkingheads.data.Talk
import com.accuratebits.tallkingheads.ui.theme.TallkingHeadsTheme
import kotlin.random.Random

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun TalksScreen(
    modifier: Modifier = Modifier,
    viewModel: TalksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is TalksUiState.Content -> {
            TalksScreen(modifier = modifier, preparedTalks = state.prepared, givenTalks = state.given, onClick = {
                viewModel.markAsGiven(it.id)
            }, onRemoveClick = {
                viewModel.remove(it.id)
            })
        }
        TalksUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TalksScreen(
    preparedTalks: List<Talk>,
    givenTalks: List<Talk>,
    modifier: Modifier = Modifier,
    onClick: (Talk) -> Unit,
    onRemoveClick: (Talk) -> Unit
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        stickyHeader {
            Text(
                "Prepared talks".uppercase(),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillParentMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                color = Color.LightGray
            )
        }
        if (preparedTalks.isNotEmpty()) {
            items(preparedTalks) { talk ->
                PreparedTalkItem(talk = talk, onClick = { onClick(talk) }, onRemoveClick = { onRemoveClick(talk) })
            }
        } else {
            item {
                Text(
                    "No prepared talks, yet!",
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(vertical = 24.dp),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }

        stickyHeader {
            Text(
                "Given talks".uppercase(),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillParentMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                color = Color.LightGray
            )
        }
        if (givenTalks.isNotEmpty()) {
            items(givenTalks) { talk ->
                TalkItem(talk = talk)
            }
        } else {
            item {
                Text(
                    "No given talks, yet!", modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(vertical = 24.dp),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TalksScreenPreview() {
    TallkingHeadsTheme {
        TalksScreen(
            listOf(1, 2, 3, 4).map {
                Talk(title = "Prepared title $it", score = Random.nextInt(1, 5))
            },
            listOf(1, 2, 3, 4).map {
                Talk(title = "Given title $it", score = Random.nextInt(1, 5))
            },
            modifier = Modifier.fillMaxSize(),
            onClick = {},
            onRemoveClick = {}
        )
    }
}