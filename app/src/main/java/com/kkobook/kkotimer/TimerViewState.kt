package com.kkobook.kkotimer

sealed class TimerViewState {
    object On : TimerViewState()
    object Off : TimerViewState()
}
