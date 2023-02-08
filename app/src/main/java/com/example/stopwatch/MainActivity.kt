package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.stopwatch.databinding.ActivityMainBinding
import com.example.stopwatch.databinding.DialogCountdownSettingBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.countdownTextView.setOnClickListener {
            showCountdownSettingDialog()
        }

        binding.stopButton.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showCountdownSettingDialog() {
        AlertDialog.Builder(this).apply {
            val dialogBinding = DialogCountdownSettingBinding.inflate(layoutInflater)
            with(dialogBinding.countdownSecondPicker) {
                maxValue = 20
                minValue = 0
            }
            setTitle("카운트다운 설정")
            setView(dialogBinding.root)
            setPositiveButton("확인", null)
            setNegativeButton("취소", null)
        }.show()
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("종료하시겠습니까?")
            setPositiveButton("네", null)
            setNegativeButton("아니요", null)
        }.show()
    }
}