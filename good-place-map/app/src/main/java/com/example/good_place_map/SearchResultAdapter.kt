package com.example.good_place_map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.good_place_map.databinding.ItemSearchResultBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128

class SearchResultAdapter(private val onClick: (LatLng) -> Unit) :
    ListAdapter<SearchItem, SearchResultAdapter.SearchItemViewHolder>(diffUtil) {
    inner class SearchItemViewHolder(private val binding: ItemSearchResultBinding) :
        ViewHolder(binding.root) {
        fun bind(item: SearchItem) {
            binding.titleTextView.text = item.title
            binding.roadAddressTextView.text = item.roadAddress
            binding.categoryTextView.text = item.category
            binding.root.setOnClickListener {
                onClick(Tm128(item.mapx.toDouble(), item.mapy.toDouble()).toLatLng())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        return SearchItemViewHolder(
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}