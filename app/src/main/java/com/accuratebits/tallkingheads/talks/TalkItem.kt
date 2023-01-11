package com.accuratebits.tallkingheads.talks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.accuratebits.tallkingheads.R
import com.accuratebits.tallkingheads.create.TalkScore
import com.accuratebits.tallkingheads.data.Talk
import com.accuratebits.tallkingheads.ui.theme.TallkingHeadsTheme
import kotlin.random.Random

@Composable
fun TalkItem(
    talk: Talk,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(talk.title)
        TalkScore(score = talk.score)
    }
}

@Composable
fun PreparedTalkItem(
    talk: Talk,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TalkItem(talk = talk, modifier = Modifier.clickable(onClick = onClick))

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onRemoveClick) {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TalkItemPreview() {
    TallkingHeadsTheme {
        TalkItem(talk = Talk(title = "Framework of the week", score = Random.nextInt(1, 5)))
    }
}

@Preview(showBackground = true)
@Composable
fun PreparedTalkItemPreview() {
    TallkingHeadsTheme {
        PreparedTalkItem(
            talk = Talk(title = "Framework of the week", score = Random.nextInt(1, 5)),
            onClick = {},
            onRemoveClick = {}
        )
    }
}