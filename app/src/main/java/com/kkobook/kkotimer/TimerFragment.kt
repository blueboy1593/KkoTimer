package com.kkobook.kkotimer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kkobook.kkotimer.TimerViewModel.Companion.MIllIS_IN_FUTURE
import com.kkobook.kkotimer.TimerViewModel.Companion.TICK_INTERVAL
import com.kkobook.kkotimer.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding
    private lateinit var viewModel: TimerViewModel
    private lateinit var timer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[TimerViewModel::class.java]

        viewModel.customTimerDuration.observe(viewLifecycleOwner) {
            binding.time.text = it.toString()
        }

        binding.buttonStart.setOnClickListener {
            viewModel.timerJob.start()
        }

        binding.buttonReset.setOnClickListener {
            viewModel.customTimerDuration.value = 10000L
        }

        return binding.root
    }

    // Don't use. This is just for CountDownTimer practice.
    private fun setUpCountDownTimer() {
        timer = object : CountDownTimer(MIllIS_IN_FUTURE, TICK_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.customTimerDuration.value = millisUntilFinished
            }

            override fun onFinish() {
                binding.time.text = "끝 끝"
            }

        }
    }

    companion object {
        fun newInstance() = TimerFragment()
    }
}