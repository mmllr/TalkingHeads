package com.accuratebits.tallkingheads.create

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.accuratebits.tallkingheads.R
import com.accuratebits.tallkingheads.ui.theme.TallkingHeadsTheme

@Composable
fun TalkScore(
    score: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        (1..5).forEach { idx ->
            Icon(
                painter = painterResource(id = if (idx > score) R.drawable.star else R.drawable.star_filled),
                contentDescription = null
            )
        }
    }
}

@Preview("Score with 3", showBackground = true)
@Composable
fun TalkScorePreview() {
    TallkingHeadsTheme {
        TalkScore(3, modifier = Modifier)
    }
}
