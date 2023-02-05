package com.example.unit_converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.unit_converter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var inputNumber = 0
    var cmToM = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inputEditText = binding.inputEditText
        val outputTextView = binding.outputTextView
        val inputUnitTextView = binding.inputUnitTextView
        val outputUnitTextView = binding.outputUnitTextView
        val swapImageButton = binding.swapImageButton

        inputEditText.addTextChangedListener { text ->
            inputNumber = if (text.isNullOrEmpty()) 0 else text.toString().toInt()

            if (cmToM) {
                outputTextView.text = inputNumber.times(0.01).toString()
            } else {
                outputTextView.text = inputNumber.times(100).toString()
            }
        }

        swapImageButton.setOnClickListener {
            cmToM = !cmToM
            if (cmToM) {
                inputUnitTextView.text = "cm"
                outputUnitTextView.text = "m"
                outputTextView.text = inputNumber.times(0.01).toString()
            } else {
                inputUnitTextView.text = "m"
                outputUnitTextView.text = "cm"
                outputTextView.text = inputNumber.times(100).toString()
            }
        }
    }
}