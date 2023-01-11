package com.accuratebits.tallkingheads.data

interface TalkClient {
    suspend fun fetchTalk(): Result<Talk>
}