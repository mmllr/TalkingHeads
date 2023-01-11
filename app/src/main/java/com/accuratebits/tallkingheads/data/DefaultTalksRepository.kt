package com.accuratebits.tallkingheads.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

class DefaultTalksRepository : TalkRepository {
    private val talks: MutableStateFlow<List<Talk>> = MutableStateFlow(listOf())

    override suspend fun save(talk: Talk) {
        talks.update {
            it.filterNot { it.id == talk.id } + listOf(talk)
        }
    }

    override suspend fun remove(talkId: UUID) {
        talks.update {
            it.filterNot { it.id == talkId }
        }
    }

    override fun getTalksStream(): Flow<List<Talk>> = talks
}