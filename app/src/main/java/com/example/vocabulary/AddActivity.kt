package com.example.vocabulary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vocabulary.databinding.ActivityAddBinding
import com.google.android.material.chip.Chip

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        binding.addButton.setOnClickListener {
            add()
        }
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

    private fun add() {
        val word = binding.wordTextInputEditText.text.toString()
        val mean = binding.meanTextInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()
        val wordPair = Word(word, mean, type)

        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.insert(wordPair)
            runOnUiThread {
                Toast.makeText(this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show()
            }
            notifyAddWord()
        }.start()
    }

    private fun notifyAddWord() {
        Intent(this, MainActivity::class.java).apply {
            putExtra("addWord", true)
            setResult(RESULT_OK, this)
        }
        finish()
    }
}