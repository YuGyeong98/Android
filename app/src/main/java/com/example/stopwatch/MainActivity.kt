package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.stopwatch.databinding.ActivityMainBinding
import com.example.stopwatch.databinding.DialogCountdownSettingBinding
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var countdownSecond = 10
    private var currentCountdownDeciSecond = countdownSecond * 10
    private var currentDeciSecond = 0
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCountdownView()

        binding.countdownTextView.setOnClickListener {
            showCountdownSettingDialog()
        }

        binding.startButton.setOnClickListener {
            start()
            binding.startButton.isVisible = false
            binding.stopButton.isVisible = false
            binding.pauseButton.isVisible = true
            binding.lapButton.isVisible = true
        }

        binding.pauseButton.setOnClickListener {
            pause()
            binding.startButton.isVisible = true
            binding.stopButton.isVisible = true
            binding.pauseButton.isVisible = false
            binding.lapButton.isVisible = false
        }

        binding.stopButton.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun initCountdownView() {
        binding.countdownTextView.text = String.format("%02d", countdownSecond)
        binding.countdownProgressBar.progress = 100
    }

    private fun showCountdownSettingDialog() {
        AlertDialog.Builder(this).apply {
            val dialogBinding = DialogCountdownSettingBinding.inflate(layoutInflater)
            with(dialogBinding.countdownSecondPicker) {
                maxValue = 20
                minValue = 0
                value = countdownSecond
            }
            setTitle("카운트다운 설정")
            setView(dialogBinding.root)
            setPositiveButton("확인") { _, _ ->
                countdownSecond = dialogBinding.countdownSecondPicker.value
                currentCountdownDeciSecond = countdownSecond * 10
                binding.countdownTextView.text = String.format("%02d", countdownSecond)
            }
            setNegativeButton("취소", null)
        }.show()
    }

    private fun start() {
        timer = timer(period = 100) { // worker 스레드
            if (currentCountdownDeciSecond == 0) {
                currentDeciSecond += 1 // 0.1초마다 증가
                val minutes = (currentDeciSecond / 10) / 60 // 1초를 60으로 나누기
                val seconds = (currentDeciSecond / 10) % 60
                val deciSeconds = currentDeciSecond % 10

                runOnUiThread { // ui 스레드
                    binding.timeTextView.text = String.format("%02d:%02d", minutes, seconds)
                    binding.tickTextView.text = deciSeconds.toString()
                    binding.countdownGroup.isVisible = false
                }
            } else {
                currentCountdownDeciSecond -= 1 // 0.1초마다 감소
                val seconds = currentCountdownDeciSecond / 10
                val progress = (currentCountdownDeciSecond / (countdownSecond * 10f)) * 100

                binding.root.post { // ui 스레드
                    binding.countdownTextView.text = String.format("%02d", seconds)
                    binding.countdownProgressBar.progress = progress.toInt()
                }
            }
        }
    }

    private fun pause() {
        timer?.cancel()
    }

    private fun stop() {
        binding.startButton.isVisible = true
        binding.stopButton.isVisible = true
        binding.pauseButton.isVisible = false
        binding.lapButton.isVisible = false

        currentDeciSecond = 0
        binding.timeTextView.text = "00:00"
        binding.tickTextView.text = "0"

        binding.countdownGroup.isVisible = true
        initCountdownView()
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("종료하시겠습니까?")
            setPositiveButton("네") { _, _ ->
                stop()
            }
            setNegativeButton("아니요", null)
        }.show()
    }
}