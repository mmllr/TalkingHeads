package com.accuratebits.tallkingheads.create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.accuratebits.tallkingheads.ui.theme.TallkingHeadsTheme

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CreateTalk(
    modifier: Modifier = Modifier,
    viewModel: CreateTalkViewModel = hiltViewModel(),
    onSaveClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is CreateTalkUiState.Edit -> {
            CreateTalk(
                title = state.talk.title,
                score = state.talk.score,
                enabled = true,
                modifier = modifier,
                onTitleChange = viewModel::editTitle,
                onScoreChange = viewModel::editScore,
                onFetchClick = viewModel::fetch,
                onSaveClick = {
                    viewModel.save()
                    onSaveClick()
                }
            )
        }
        CreateTalkUiState.Loading -> {
            CreateTalk(
                title = "",
                score = 0,
                enabled = false,
                modifier = modifier,
                onTitleChange = {},
                onScoreChange = {},
                onFetchClick = {},
                onSaveClick = {}
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTalk(
    title: String,
    score: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onTitleChange: (String) -> Unit,
    onScoreChange: (Int) -> Unit,
    onFetchClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Edit Talk", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onSaveClick, enabled = enabled) {
                    Text("Save")
                }
            }

            Card {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = title,
                        placeholder = { Text("Title") },
                        onValueChange = onTitleChange,
                        enabled = enabled,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )

                    Row {
                        Column {
                            Text("Buzzword-Score")
                            TalkScore(score = score)
                        }

                        Spacer(modifier = modifier.weight(1f))

                        Stepper(value = score, enabled = enabled, valueRange = 0..5, onValueChange = onScoreChange)
                    }
                }
            }

            Button(
                onClick = onFetchClick,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Fetch from the internet™")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTalkPreview() {
    var title by remember {
        mutableStateOf("")
    }
    var score by remember {
        mutableStateOf(0)
    }
    TallkingHeadsTheme {
        CreateTalk(
            title,
            score,
            true,
            onTitleChange = { title = it },
            onScoreChange = { score = it },
            onFetchClick = {},
            onSaveClick = {}
        )
    }
}