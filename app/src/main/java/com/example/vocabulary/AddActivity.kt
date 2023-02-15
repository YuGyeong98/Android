package com.example.vocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vocabulary.databinding.ActivityAddBinding
import com.google.android.material.chip.Chip

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        val types = listOf("명사", "동사", "대명사", "형용사", "부사", "감탄사", "전치사", "접속사")
        binding.typeChipGroup.apply {
            types.forEach {
                addView(createChip(it))
            }
        }
    }

    private fun createChip(type: String) =
        Chip(this).apply {
            text = type
            isCheckable = true
        }
}