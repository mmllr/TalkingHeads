package com.accuratebits.tallkingheads.data

import kotlinx.coroutines.flow.Flow
import java.util.*

interface TalkRepository {
    suspend fun save(talk: Talk)
    suspend fun remove(talkId: UUID)
    fun getTalksStream(): Flow<List<Talk>>
}