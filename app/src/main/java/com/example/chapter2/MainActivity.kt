package com.example.chapter2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chapter2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetButton.setOnClickListener {
            number = 0
            binding.numberTextView.text = number.toString()
        }

        binding.plusButton.setOnClickListener {
            number += 1
            binding.numberTextView.text = number.toString()
        }
    }
}