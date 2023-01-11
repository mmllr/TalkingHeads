package com.accuratebits.tallkingheads.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accuratebits.tallkingheads.create.CreateTalkUiState.Edit
import com.accuratebits.tallkingheads.create.CreateTalkUiState.Loading
import com.accuratebits.tallkingheads.data.Talk
import com.accuratebits.tallkingheads.data.TalkClient
import com.accuratebits.tallkingheads.data.TalkRepository
import com.accuratebits.tallkingheads.di.UUIDGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

sealed interface CreateTalkUiState {
    object Loading : CreateTalkUiState
    data class Edit(val talk: Talk) : CreateTalkUiState
}

val CreateTalkUiState.canSave: Boolean
    get() = when (this) {
        is Edit -> talk.title.isNotBlank()
        Loading -> false
    }

@HiltViewModel
class CreateTalkViewModel @Inject constructor(
    @UUIDGenerator private val uuidGen: () -> UUID,
    private val repository: TalkRepository,
    private val talkClient: TalkClient
) :
    ViewModel() {
    private val talkFlow = MutableStateFlow(Talk(uuidGen(), title = "", score = 0))
    private val isFetchingFLow = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CreateTalkUiState> = merge<CreateTalkUiState>(
        talkFlow.map { Edit(it) },
        isFetchingFLow.flatMapLatest {
            flow {
                if (it) {
                    emit(Loading)
                    talkFlow.value = talkClient.fetchTalk().fold(
                        onSuccess = { it },
                        onFailure = { Talk(uuidGen(), "", 0) }
                    )
                    isFetchingFLow.value = false
                }
            }
        }
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Loading
    )

    fun editTitle(title: String) {
        talkFlow.update {
            it.copy(title = title)
        }
    }

    fun editScore(score: Int) {
        if (!(0..5).contains(score)) {
            return
        }
        talkFlow.update {
            it.copy(score = score)
        }
    }

    fun fetch() {
        isFetchingFLow.value = true
    }

    fun save() {
        when (val state = uiState.value) {
            is Edit -> {
                viewModelScope.launch {
                    repository.save(state.talk)
                    talkFlow.value = Talk(uuidGen(), "", 0)
                }
            }
            Loading -> return
        }

    }
}