package com.example.unit_converter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.unit_converter.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var inputNumber = BigDecimal("0")
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
            inputNumber =
                if (text.isNullOrEmpty()) BigDecimal("0") else text.toString().toBigDecimal()

            if (cmToM) {
                outputTextView.text = conversion("0.01")
            } else {
                outputTextView.text = conversion("100")
            }
        }

        swapImageButton.setOnClickListener {
            cmToM = !cmToM
            if (cmToM) {
                inputUnitTextView.text = "cm"
                outputUnitTextView.text = "m"
                outputTextView.text = conversion("0.01")
            } else {
                inputUnitTextView.text = "m"
                outputUnitTextView.text = "cm"
                outputTextView.text = conversion("100")
            }
        }
    }

    private fun conversion(value: String): String {
        inputNumber = inputNumber.times(BigDecimal(value))

        return if (inputNumber.scale() < 6) {
            inputNumber.toString()
        } else {
            inputNumber.setScale(6, RoundingMode.HALF_EVEN).toString()
        }
    }
}