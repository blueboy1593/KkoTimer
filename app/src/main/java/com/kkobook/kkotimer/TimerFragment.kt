package com.kkobook.kkotimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kkobook.kkotimer.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding
    private lateinit var viewModel: TimerViewModel
    private lateinit var soundPool: SoundPool
    private var kkobookSoundId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[TimerViewModel::class.java]

        prepareObserve()

        binding.buttonStart.setOnClickListener {
            viewModel.startTimer()
            setAlarm()
            soundPool.play(kkobookSoundId!!, 10f, 10f, 1, 0, 1f)
        }

        binding.buttonStop.setOnClickListener {
            viewModel.stopTimer()
            soundPool.play(kkobookSoundId!!, 10f, 10f, 1, 0, 1f)
        }

        binding.buttonReset.setOnClickListener {
            viewModel.resetTimer()
            binding.timerView.resetTimer()
            soundPool.play(kkobookSoundId!!, 10f, 10f, 1, 0, 1f)
        }

        binding.timerView.setViewModel(viewModel)

        initSounds()

        return binding.root
    }

    private fun prepareObserve() {
        viewModel.customTimerDuration.observe(viewLifecycleOwner) {
            val timeText = convertTime(it)
            binding.time.text = timeText
        }

        viewModel.isTimerEnd.observe(viewLifecycleOwner) {
            if (it) {
                alarmTimerEnd()
            }
        }

        viewModel.timerViewState.observe(viewLifecycleOwner) {
            when (it) {
                TimerViewState.On -> binding.timerView.drawEyeClose()
                TimerViewState.Off -> binding.timerView.drawEyeOpen()
            }
        }
    }

    private fun initSounds() {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2).build()
        } else {
            SoundPool(2, AudioManager.STREAM_MUSIC, 0)
        }
        kkobookSoundId = soundPool.load(context, R.raw.kkobook_kkobook, 1)
    }

    private fun convertTime(timeInMillis: Long): String {
        val timeInSec = timeInMillis / 1000
        val min = timeInSec.div(60)
        val sec = timeInSec.rem(60)
        return "$min 분 $sec 초"
    }

    private fun alarmTimerEnd() {
        soundPool.play(kkobookSoundId!!, 10f, 10f, 1, 4, 1f)
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // sound파일들이 메모리에서 제거된다.
        soundPool.release()
    }

    private fun setAlarm() {
        val alarmManager = context?.let { getSystemService(it, AlarmManager::class.java) } as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("kkog", SystemClock.elapsedRealtime().toString())
            Log.d("kkog", viewModel.customTimerDuration.value!!.toString())
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + viewModel.customTimerDuration.value!!,
                pendingIntent
            )
        }
    }

    companion object {
        fun newInstance() = TimerFragment()
    }

}