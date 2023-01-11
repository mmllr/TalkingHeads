package com.accuratebits.tallkingheads.talks

import com.accuratebits.tallkingheads.data.Talk
import com.accuratebits.tallkingheads.helpers.FakeTalkRepository
import com.accuratebits.tallkingheads.helpers.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TalksViewModelTest {
    private lateinit var sut: TalksViewModel
    private lateinit var repository: FakeTalkRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        repository = FakeTalkRepository()
        sut = TalksViewModel(repository)
    }

    @Test
    fun `It loads the talks`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }
        assertEquals(TalksUiState.Loading, sut.uiState.value)

        val prepared = Talk(title = "Talk a", score = 3, given = false)
        val given = Talk(title = "Talk b", score = 5, given = true)

        repository.talks.send(listOf(given, prepared))

        assertEquals(TalksUiState.Content(listOf(prepared), listOf(given)), sut.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun `Mark a talk as given will save it in the repository`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }
        val prepared = Talk(title = "Talk a", score = 3, given = false)
        val given = Talk(title = "Talk b", score = 5, given = true)
        repository.talks.send(listOf(given, prepared))
        assertEquals(TalksUiState.Content(listOf(prepared), listOf(given)), sut.uiState.value)

        // marking a given talk will do nothing
        sut.markAsGiven(given.id)

        assertNull(repository.savedTalk)

        sut.markAsGiven(prepared.id)

        assertEquals(prepared.copy(given = true), repository.savedTalk)

        collectJob.cancel()
    }

    @Test
    fun `Deleting a talk`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }
        val preparedA = Talk(title = "Talk a", score = 3, given = false)
        val preparedB = Talk(title = "Talk b", score = 3, given = false)
        val given = Talk(title = "Given talk", score = 5, given = true)
        repository.talks.send(listOf(given, preparedA, preparedB))
        assertEquals(TalksUiState.Content(listOf(preparedA, preparedB), listOf(given)), sut.uiState.value)

        // marking a given talk will do nothing
        sut.remove(given.id)

        assertNull(repository.removedTalkId)

        sut.remove(preparedB.id)

        assertEquals(preparedB.id, repository.removedTalkId)

        collectJob.cancel()
    }
}