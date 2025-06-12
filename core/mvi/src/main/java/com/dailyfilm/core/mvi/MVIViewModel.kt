package com.dailyfilm.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * ## 개요
 * - MVI 패턴을 ViewModel에서 활용할 수 있도록 돕는 추상 클래스 입니다.
 *
 * ## 구현 세부 사항
 * - [event]에서 `receiveAsFlow` 함수 호출하여 UiState를 생성하면 됩니다.
 * - 이전 UiState를 통해 새 UiState를 만들 때는 [runningFold][kotlinx.coroutines.flow.runningFold] 함수를 활용합니다.
 * - `reduce`는 순수 함수로 유지하기 위해 `handleSideEffect`를 제공합니다.
 *
 * ## 사용법
 * - UiEvent는 [postUiEvent]로 발생시키고, SideEffect는 [postSideEffect]로 발생시킵니다.
 * - [sideEffect]는 [Flow][kotlinx.coroutines.flow.Flow] 타입이기 때문에 Compose에서 State로 변환하여 처리 가능합니다.
 * ### 예시
 * ```kotlin
 * class DailyFilmViewModel : MVIViewModel<DailyFilmUiEvent, DailyFilmSideEffect>() {
 *
 *     val updateUiState = event.receiveAsFlow()
 *         .onEach(::handleSideEffect)
 *         .runningFold(DailyFilmUiState.Loading, ::reduce)
 *         .stateIn(
 *             scope = viewModelScope,
 *             started = SharingStarted.WhileSubscribed(5_000),
 *             initialValue = DailyFilmUiState.Loading,
 *         )
 *
 *
 *
 *     override fun handleSideEffect(event: DailyFilmUiEvent) {
 *         when (event) {
 *             DailyFilmUiEvent.UpdateText -> Unit
 *             DailyFilmUiEvent.ClickNavigateButton -> postSideEffect(NavigateToCalendar)
 *             ...
 *         }
 *     }
 *
 *     override fun reduce(
 *         state: DailyFilmUiState,
 *         event: DailyFilmUiEvent
 *     ): DailyFilmUiState = when (event) {
 *         is UpdateText -> state.copy(text = event.text)
 *         ...
 *     }
 *
 *
 *
 * // data class로 선언하여 관리할 수도 있습니다
 * sealed interface DailyFilmUiState {
 *     data object Loading : DailyFilmUiState
 *     data class Loaded(
 *         val text: String,
 *         val num: Int,
 *         ...
 *     ): DailyFilmUiState
 * }
 *
 * sealed interface DailyFilmUiEvent {
 *     data class UpdateText(newText: String): DailyFilmUiEvent
 *     data class UpdateNumber(newNumber: Int): DailyFilmUiEvent
 *     data object ClickNavigateButton: DailyFilmUiEvent
 * }
 *
 * sealed interface DailyFilmSideEffect {
 *     data object None : DailyFilmSideEffect
 *     data object NavigateToCalendar : DailyFilmSideEffect
 * }
 * ```
 *
 */
abstract class MVIViewModel<S, E, SE>(extraBufferCapacity: Int = 1) : ViewModel() {

    protected val event = Channel<E>(Channel.BUFFERED)

    private val _sideEffect = MutableSharedFlow<SE>(extraBufferCapacity = extraBufferCapacity)
    val sideEffect = _sideEffect.asSharedFlow()

    protected abstract fun handleSideEffect(event: E)

    protected abstract fun reduce(state: S, event: E): S

    fun postUiEvent(uiEvent: E) {
        viewModelScope.launch {
            event.send(uiEvent)
        }
    }

    fun postSideEffect(sideEffect: SE) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

}