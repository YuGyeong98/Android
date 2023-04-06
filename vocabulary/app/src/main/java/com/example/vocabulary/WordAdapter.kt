package com.example.vocabulary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabulary.databinding.ItemWordBinding

class WordAdapter(
    val list: MutableList<Word>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    class WordViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(word: Word) {
            binding.apply {
                wordTextView.text = word.word
                meanTextView.text = word.mean
                typeChip.text = word.type
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWordBinding.inflate(inflater, parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = list[position]
        holder.bind(word)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(word)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener {
        fun onClick(word: Word)
    }
}