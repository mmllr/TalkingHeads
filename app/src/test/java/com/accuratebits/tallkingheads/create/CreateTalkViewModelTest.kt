package com.accuratebits.tallkingheads.create

import com.accuratebits.tallkingheads.data.Talk
import com.accuratebits.tallkingheads.data.TalkClient
import com.accuratebits.tallkingheads.helpers.FakeTalkRepository
import com.accuratebits.tallkingheads.helpers.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTalkViewModelTest {
    private lateinit var talkClient: FakeTalkClient
    private lateinit var fixedId: UUID
    private lateinit var repository: FakeTalkRepository
    private lateinit var sut: CreateTalkViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        repository = FakeTalkRepository()
        talkClient = FakeTalkClient()
        fixedId = UUID.randomUUID()
        sut = CreateTalkViewModel(uuidGen = { fixedId }, repository, talkClient)
    }

    @Test
    fun `Create talk flow`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }
        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "", score = 0)), sut.uiState.value)

        sut.editTitle("Title")

        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "Title", score = 0)), sut.uiState.value)

        val score = Random.nextInt(0, 5)
        sut.editScore(score)

        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "Title", score = score)), sut.uiState.value)

        sut.editScore(-1)

        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "Title", score = score)), sut.uiState.value)

        sut.editScore(6)

        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "Title", score = score)), sut.uiState.value)

        sut.fetch()

        assertEquals(CreateTalkUiState.Loading, sut.uiState.value)

        val talk = Talk(title = "Fetched talk", score = 3)
        talkClient.talkChannel.trySend(Result.success(talk))

        assertEquals(CreateTalkUiState.Edit(talk), sut.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun `Fetching with failure will result in new empty talk`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }
        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "", score = 0)), sut.uiState.value)
        sut.editTitle("Title")
        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "Title", score = 0)), sut.uiState.value)

        sut.fetch()

        assertEquals(CreateTalkUiState.Loading, sut.uiState.value)

        fixedId = UUID.randomUUID()
        talkClient.talkChannel.trySend(Result.failure(RuntimeException("Test error")))

        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "", score = 0)), sut.uiState.value)

        sut.editTitle("New title")

        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "New title", score = 0)), sut.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun `Saving a talk`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }
        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "", score = 0)), sut.uiState.value)
        sut.editTitle("Title")
        sut.editScore(4)
        val savedId = fixedId
        assertEquals(CreateTalkUiState.Edit(Talk(savedId, title = "Title", score = 4)), sut.uiState.value)

        fixedId = UUID.randomUUID()
        sut.save()

        assertEquals(Talk(savedId, "Title", 4), repository.savedTalk)
        assertEquals(CreateTalkUiState.Edit(Talk(fixedId, title = "", score = 0)), sut.uiState.value)

        collectJob.cancel()
    }
}

class FakeTalkClient : TalkClient {
    val talkChannel = Channel<Result<Talk>>()

    override suspend fun fetchTalk(): Result<Talk> {
        return talkChannel.receive()
    }
}

