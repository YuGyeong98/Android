package com.example.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.databinding.ItemNewsBinding

class NewsAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<NewsModel, NewsAdapter.NewsViewHolder>(diffUtil) {
    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewsModel) {
            binding.titleTextView.text = item.title
            Glide.with(binding.thumbnailImageView)
                .load(item.imageUrl)
                .into(binding.thumbnailImageView)
            binding.root.setOnClickListener {
                onClick(item.link)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = currentList[position]
        holder.bind(news)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NewsModel>() {
            override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}