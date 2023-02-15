package com.example.vocabulary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabulary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.goToAddActivity.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        wordAdapter = WordAdapter(mutableListOf(Word("apple", "사과", "명사"), Word("banana", "바나나", "명사")), this)
        binding.wordRecyclerView.apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            val divider = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }
    }

    override fun onClick(word: Word) {
        binding.wordTextView.text = word.word
        binding.meanTextView.text = word.mean
    }
}