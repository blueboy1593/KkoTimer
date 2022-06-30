package com.kkobook.kkotimer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class TimerViewModel : ViewModel() {
    val customTimerDuration: MutableLiveData<Long> = MutableLiveData(MIllIS_IN_FUTURE)
    private var oldTimeMills: Long = 0

    val timerJob: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(Dispatchers.IO) {
            oldTimeMills = System.currentTimeMillis()
            while (customTimerDuration.value!! > 0L) {
                val delayMills = System.currentTimeMillis() - oldTimeMills
                if (delayMills == TICK_INTERVAL) {
                    customTimerDuration.postValue(customTimerDuration.value!! - delayMills)
                    oldTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    companion object {
        const val MIllIS_IN_FUTURE = 10000L
        const val TICK_INTERVAL = 1000L
    }
}
