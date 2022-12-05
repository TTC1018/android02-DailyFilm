package com.boostcamp.dailyfilm.presentation.selectvideo

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.boostcamp.dailyfilm.data.model.Result
import com.boostcamp.dailyfilm.data.model.VideoItem
import com.boostcamp.dailyfilm.data.selectvideo.GalleryVideoRepository
import com.boostcamp.dailyfilm.presentation.calendar.CalendarActivity
import com.boostcamp.dailyfilm.presentation.calendar.model.DateModel
import com.boostcamp.dailyfilm.presentation.uploadfilm.model.DateAndVideoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectVideoViewModel @Inject constructor(
    private val selectVideoRepository: GalleryVideoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), VideoSelectListener {

    val dateModel = savedStateHandle.get<DateModel>(CalendarActivity.KEY_DATE_MODEL)

    override val viewTreeLifecycleScope: CoroutineScope
        get() = viewModelScope

    private val _videosState =
        MutableStateFlow(Result.Success<PagingData<VideoItem>>(PagingData.empty()))
    val videosState: StateFlow<Result<*>> get() = _videosState

    private val _selectedVideo = MutableStateFlow<VideoItem?>(null)
    override val selectedVideo = _selectedVideo.asStateFlow()

    private var clickSound = true

    private val _eventFlow = MutableSharedFlow<SelectVideoEvent>()
    val eventFlow: SharedFlow<SelectVideoEvent> = _eventFlow.asSharedFlow()

    fun navigateToUpload() {
        viewModelScope.launch {
            selectedVideo.value?.let { selectedVideoItem ->
                if (dateModel != null) {
                    event(
                        SelectVideoEvent.NextButtonResult(
                            DateAndVideoModel(
                                selectedVideoItem.uri,
                                dateModel.getDate()
                            )
                        )
                    )
                }
            }
        }
    }

    fun controlSound() {
        clickSound = !clickSound
        event(SelectVideoEvent.ControlSoundResult(clickSound))
    }

    fun backToMain() {
        event(SelectVideoEvent.BackButtonResult(true))
    }

    fun loadVideo() {
        selectVideoRepository.loadVideo().cachedIn(viewModelScope).onEach {
            _videosState.value = Result.Success(it)
        }.launchIn(viewModelScope)
    }

    private fun event(event: SelectVideoEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    override fun chooseVideo(videoItem: VideoItem?) {
        viewModelScope.launch {
            _selectedVideo.emit(videoItem)
        }
    }

}

sealed class SelectVideoEvent {
    data class NextButtonResult(val dateAndVideoModelItem: DateAndVideoModel) : SelectVideoEvent()
    data class BackButtonResult(val result: Boolean) : SelectVideoEvent()
    data class ControlSoundResult(val result: Boolean) : SelectVideoEvent()
}

