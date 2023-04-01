package com.example.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.databinding.ActivityMainBinding
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

        newsAdapter = NewsAdapter()
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        val service = APIClient.retrofit.create(NewsService::class.java)
        service.politicsNews().submitList()
    }

    private fun Call<News>.submitList() {
        enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                newsAdapter.submitList(response.body()?.channel?.items)
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}