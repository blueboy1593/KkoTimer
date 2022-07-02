package com.kkobook.kkotimer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class TimerViewModel : ViewModel() {
    private var timerJob: Job
    private var oldTimeMills: Long = 0
    val isTimerEnd: MutableLiveData<Boolean> = MutableLiveData(false)
    val timerViewState: MutableLiveData<TimerViewState> = MutableLiveData(TimerViewState.Off)
    val customTimerDuration: MutableLiveData<Long> = MutableLiveData(MIllIS_IN_FUTURE)

    init {
        timerJob = Job()
    }

    fun startTimer() {
        isTimerEnd.postValue(false)
        if (customTimerDuration.value!! > 0L) {
            timerViewState.postValue(TimerViewState.On)
        }

        timerJob = Job()
        timerJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                oldTimeMills = System.currentTimeMillis()
                while (customTimerDuration.value!! > 0L && isActive) {
                    val delayMills = System.currentTimeMillis() - oldTimeMills
                    if (delayMills == TICK_INTERVAL) {
                        customTimerDuration.postValue(customTimerDuration.value!! - delayMills)
                        oldTimeMills = System.currentTimeMillis()
                    }
                }
                if (isActive) {
                    isTimerEnd.postValue(true)
                    resetTimer()
                }
            }
        }
    }

    fun stopTimer() {
        timerJob.cancel()
        timerViewState.postValue(TimerViewState.Off)
    }

    fun resetTimer() {
        timerJob.cancel()
        timerViewState.postValue(TimerViewState.Off)
        customTimerDuration.postValue(0L)
    }

    fun setTime(degree: Float) {
        val longDegree = (degree * 10000).toLong()
        customTimerDuration.postValue(longDegree)
    }

    companion object {
        const val MIllIS_IN_FUTURE = 0L
        const val TICK_INTERVAL = 1000L
    }
}
