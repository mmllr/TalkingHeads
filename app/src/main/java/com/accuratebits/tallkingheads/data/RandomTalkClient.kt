package com.accuratebits.tallkingheads.data

import kotlinx.coroutines.delay

class RandomTalkClient : TalkClient {
    private val talks = listOf<Talk>(
        Talk(title = "Is testing really dead?", score = 3),
        Talk(title = "Enterprise Swift on the Server", score = 5),
        Talk(title = "JSON parsing from scratch in C89", score = 2),
        Talk(title = "C++ GUI development", score = 1),
        Talk(title = "Framework of the week", score = 5)
    )

    override suspend fun fetchTalk(): Result<Talk> {
        delay(500)
        return Result.success(talks.random())
    }
}