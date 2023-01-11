package com.accuratebits.tallkingheads.helpers

import com.accuratebits.tallkingheads.data.Talk
import com.accuratebits.tallkingheads.data.TalkRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.*

class FakeTalkRepository : TalkRepository {
    var removedTalkId: UUID? = null
    var savedTalk: Talk? = null
    val talks = Channel<List<Talk>>()

    override suspend fun save(talk: Talk) {
        savedTalk = talk
    }

    override suspend fun remove(talkId: UUID) {
        removedTalkId = talkId
    }

    override fun getTalksStream(): Flow<List<Talk>> = talks.receiveAsFlow()
}