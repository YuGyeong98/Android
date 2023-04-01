package com.example.news

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.databinding.ActivityMainBinding
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newsAdapter = NewsAdapter { url ->
            startActivity(Intent(this, WebViewActivity::class.java).putExtra("url", url))
        }
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        val service = APIClient.retrofit.create(NewsService::class.java)
        binding.politicsChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.politicsChip.isChecked = true
            service.politicsNews().submitList()
        }
        binding.economyChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.economyChip.isChecked = true
            service.economyNews().submitList()
        }
        binding.societyChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.societyChip.isChecked = true
            service.societyNews().submitList()
        }
        binding.lifeChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.lifeChip.isChecked = true
            service.lifeNews().submitList()
        }
        binding.globalChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.globalChip.isChecked = true
            service.globalNews().submitList()
        }
        binding.entertainmentChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.entertainmentChip.isChecked = true
            service.entertainmentNews().submitList()
        }
        binding.sportsChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.sportsChip.isChecked = true
            service.sportsNews().submitList()
        }

        service.politicsNews().submitList()
    }

    private fun Call<News>.submitList() {
        enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val list = response.body()?.channel?.items.orEmpty().transform()
                newsAdapter.submitList(list)

                list.forEachIndexed { index, newsModel ->
                    Thread {
                        try {
                            val jsoup = Jsoup.connect(newsModel.link).get()
                            val elements = jsoup.select("meta[property^=og:]") // 태그명[속성명^=속성값] - 속성명이 특정 속성값으로 시작하는 모든 요소
                            val ogImageNode = elements.find { node ->
                                node.attr("property") == "og:image"
                            }
                            val imageUrl = ogImageNode?.attr("content")
                            newsModel.imageUrl = imageUrl
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                        runOnUiThread {
                            newsAdapter.notifyItemChanged(index)
                        }
                    }.start()
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}