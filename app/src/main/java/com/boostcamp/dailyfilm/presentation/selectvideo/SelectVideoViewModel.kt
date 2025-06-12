package com.boostcamp.dailyfilm.presentation.selectvideo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.boostcamp.dailyfilm.data.model.VideoItem
import com.boostcamp.dailyfilm.data.selectvideo.GalleryVideoRepository
import com.boostcamp.dailyfilm.presentation.DailyFilmDestination
import com.boostcamp.dailyfilm.presentation.playfilm.model.EditState
import com.boostcamp.dailyfilm.presentation.uploadfilm.model.DateAndVideoModel
import com.dailyfilm.core.mvi.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SelectVideoViewModel @Inject constructor(
    private val selectVideoRepository: GalleryVideoRepository,
    savedStateHandle: SavedStateHandle
) : MVIViewModel<SelectVideoUiState, SelectVideoUiEvent, SelectVideoSideEffect>() {

    val dateModel = savedStateHandle.toRoute<DailyFilmDestination.SelectVideo>().dateModel
    val calendarIndex = savedStateHandle.toRoute<DailyFilmDestination.SelectVideo>().calendarIndex
    val editState = savedStateHandle.toRoute<DailyFilmDestination.SelectVideo>().editState

    private val initialValue = SelectVideoUiState(
        dateModel = dateModel,
        calendarIndex = calendarIndex,
        editState = editState,
    )

    val uiState = event.receiveAsFlow()
        .onEach(::handleSideEffect)
        .runningFold(initialValue, ::reduce)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = initialValue,
        )

    override fun handleSideEffect(event: SelectVideoUiEvent) {
        when (event) {
            SelectVideoUiEvent.ChangeSoundState -> Unit
            SelectVideoUiEvent.GetVideos -> postSideEffect(SelectVideoSideEffect.LoadVideos)
            is SelectVideoUiEvent.LoadedVideos -> Unit
            is SelectVideoUiEvent.SelectVideo -> Unit
        }
    }

    override fun reduce(
        state: SelectVideoUiState,
        event: SelectVideoUiEvent
    ): SelectVideoUiState = when (event) {
        SelectVideoUiEvent.ChangeSoundState -> state.copy(mute = state.mute.not())
        SelectVideoUiEvent.GetVideos -> state
        is SelectVideoUiEvent.LoadedVideos -> state.copy(
            videoItems = event.videoItems.cachedIn(viewModelScope),
        )

        is SelectVideoUiEvent.SelectVideo -> state.copy(
            selectedVideo = event.videoItem,
        )
    }

    fun navigateToUpload() {
        viewModelScope.launch {
//            // TODO URI+date 정보가지고 다음 화면 이동
        }
    }

    fun controlSound() {
        postUiEvent(SelectVideoUiEvent.ChangeSoundState)
    }

    fun backToMain() {
        // TODO Navigate
    }

    fun loadVideo() {
        postUiEvent(SelectVideoUiEvent.LoadedVideos(selectVideoRepository.loadVideo()))
    }

    fun chooseVideo(videoItem: VideoItem) {
        postUiEvent(SelectVideoUiEvent.SelectVideo(videoItem))
    }

}

sealed class SelectVideoEvent {
    data class NextButtonResult(val dateAndVideoModelItem: DateAndVideoModel) : SelectVideoEvent()
    data class BackButtonResult(val result: Boolean) : SelectVideoEvent()
    data class ControlSoundResult(val result: Boolean) : SelectVideoEvent()
}

internal data class SelectVideoUiState(
    val dateModel: DateNavigationModel,
    val calendarIndex: Int,
    val editState: EditState,
    val videoItems: Flow<PagingData<VideoItem>> = emptyFlow(),
    val selectedVideo: VideoItem? = null,
    val mute: Boolean = false,
)

internal sealed interface SelectVideoUiEvent {
    data object ChangeSoundState : SelectVideoUiEvent
    data object GetVideos : SelectVideoUiEvent
    data class LoadedVideos(
        val videoItems: Flow<PagingData<VideoItem>>,
    ) : SelectVideoUiEvent

    data class SelectVideo(
        val videoItem: VideoItem,
    ) : SelectVideoUiEvent
}

internal sealed interface SelectVideoSideEffect {
    data object LoadVideos : SelectVideoSideEffect
}

