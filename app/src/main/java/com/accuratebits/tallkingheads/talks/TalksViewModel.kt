package com.accuratebits.tallkingheads.talks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accuratebits.tallkingheads.data.Talk
import com.accuratebits.tallkingheads.data.TalkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

sealed interface TalksUiState {
    object Loading : TalksUiState
    data class Content(val prepared: List<Talk>, val given: List<Talk>) : TalksUiState
}

@HiltViewModel
class TalksViewModel @Inject constructor(private val repository: TalkRepository) : ViewModel() {
    val uiState: StateFlow<TalksUiState> = repository.getTalksStream().map { talks ->
        val (given, prepared) = talks.partition { it.given }
        TalksUiState.Content(prepared, given)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TalksUiState.Loading
    )

    fun markAsGiven(talkId: UUID) {
        val state = uiState.value
        if (state is TalksUiState.Content) {
            state.prepared.find { it.id == talkId }?.let { talk ->
                viewModelScope.launch {
                    repository.save(talk.copy(given = true))
                }
            }
        }
    }

    fun remove(talkId: UUID) {
        val state = uiState.value
        if (state is TalksUiState.Content) {
            state.prepared.find { it.id == talkId }?.let { talk ->
                viewModelScope.launch {
                    repository.remove(talkId)
                }
            }
        }
    }
}