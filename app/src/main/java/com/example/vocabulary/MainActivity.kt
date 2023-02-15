package com.example.vocabulary

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabulary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter
    private val updateAddWordResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val addWord = result.data?.getBooleanExtra("addWord", false) ?: false
            if (result.resultCode == RESULT_OK && addWord) {
                updateAddWord()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.goToAddActivity.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            updateAddWordResult.launch(intent)
        }
    }

    private fun initRecyclerView() {
        wordAdapter = WordAdapter(mutableListOf(), this)
        binding.wordRecyclerView.apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            val divider = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }

        Thread {
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()
            wordAdapter.list.addAll(list)
            runOnUiThread {
                wordAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun updateAddWord() {
        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.getLatestWord()?.let {
                wordAdapter.list.add(0, it)
                runOnUiThread {
                    wordAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    override fun onClick(word: Word) {
        binding.wordTextView.text = word.word
        binding.meanTextView.text = word.mean
    }
}