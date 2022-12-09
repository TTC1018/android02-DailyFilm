package com.boostcamp.dailyfilm.presentation.playfilm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.boostcamp.dailyfilm.presentation.calendar.model.DateModel
import javax.inject.Inject

class PlayFilmViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val dateModel = savedStateHandle.get<DateModel>(PlayFilmFragment.KEY_DATE_MODEL)
        ?: throw IllegalStateException("PlayFilmViewModel - DateModel is null")

    private val _isContentShowed = MutableLiveData(true)
    val isContentShowed: LiveData<Boolean> get() = _isContentShowed

    private val _isMuted = MutableLiveData(false)
    val isMuted: LiveData<Boolean> get() = _isMuted

    fun changeShowState(){
        _isContentShowed.value = _isContentShowed.value?.not()
    }

    fun changeMuteState(){
        _isMuted.value = _isMuted.value?.not()
    }

}