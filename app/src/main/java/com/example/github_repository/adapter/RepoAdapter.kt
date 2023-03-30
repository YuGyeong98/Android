package com.example.github_repository.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.github_repository.databinding.ItemRepoBinding
import com.example.github_repository.model.Repo

class RepoAdapter : ListAdapter<Repo, RepoAdapter.RepoViewHolder>(diffUtil) {
    inner class RepoViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Repo) {
            binding.nameTextView.text = item.name
            binding.descriptionTextView.text = item.description
            binding.starCountTextView.text = item.starCount.toString()
            binding.forkCountTextView.text = item.forkCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = currentList[position]
        holder.bind(repo)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }
}