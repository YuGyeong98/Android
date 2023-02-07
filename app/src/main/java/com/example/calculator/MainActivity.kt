package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calculator.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firstNumberText = StringBuilder("")
    private val secondNumberText = StringBuilder("")
    private val operatorText = StringBuilder("")
    private val decimalFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun numberClicked(view: View) {
        val numberString = (view as? Button)?.text.toString()
        val numberText = if (operatorText.isEmpty()) firstNumberText else secondNumberText

        numberText.append(numberString)
        updateEquationTextView()
    }

    fun operatorClicked(view: View) {
        val operatorString = (view as? Button)?.text.toString()

        if (firstNumberText.isEmpty()) {
            Toast.makeText(this, "숫자를 먼저 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (operatorText.isNotEmpty()) {
            Toast.makeText(this, "1개의 연산자만 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (secondNumberText.isNotEmpty()) {
            Toast.makeText(this, "1개의 연산자에 대해서만 연산이 가능합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        operatorText.append(operatorString)
        updateEquationTextView()
    }

    fun clearClicked(view: View) {
        firstNumberText.clear()
        operatorText.clear()
        secondNumberText.clear()

        updateEquationTextView()
        binding.resultTextView.text = ""
    }

    fun equalClicked(view: View) {
        if (firstNumberText.isEmpty() || secondNumberText.isEmpty() || operatorText.isEmpty()) {
            Toast.makeText(this, "완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val firstNumber = firstNumberText.toString().toBigDecimal()
        val secondNumber = secondNumberText.toString().toBigDecimal()

        binding.resultTextView.text = when (operatorText.toString()) {
            "+" -> decimalFormat.format(firstNumber + secondNumber)
            "-" -> decimalFormat.format(firstNumber - secondNumber)
            "×" -> decimalFormat.format(firstNumber * secondNumber)
            "÷" -> decimalFormat.format(
                if (secondNumber != BigDecimal("0")) {
                    firstNumber / secondNumber
                } else {
                    Toast.makeText(this, "0으로 나눌 수 없어요.", Toast.LENGTH_SHORT).show()
                    return
                }
            )
            "%" -> decimalFormat.format(
                if (secondNumber != BigDecimal("0")) {
                    firstNumber % secondNumber
                } else {
                    Toast.makeText(this, "0으로 나눌 수 없어요.", Toast.LENGTH_SHORT).show()
                    return
                }
            )
            else -> "잘못된 수식 입니다."
        }
    }

    private fun updateEquationTextView() {
        val firstFormattedNumber = if (firstNumberText.isNotEmpty()) decimalFormat.format(
            firstNumberText.toString().toBigDecimal()
        ) else ""
        val secondFormattedNumber = if (secondNumberText.isNotEmpty()) decimalFormat.format(
            secondNumberText.toString().toBigDecimal()
        ) else ""

        binding.equationTextView.text = "$firstFormattedNumber $operatorText $secondFormattedNumber"
    }
}