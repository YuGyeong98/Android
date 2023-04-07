package com.example.chat.chatroomlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.ItemChatRoomBinding

class ChatRoomListAdapter :
    ListAdapter<ChatRoomItem, ChatRoomListAdapter.ChatRoomListViewHolder>(diffUtil) {
    inner class ChatRoomListViewHolder(private val binding: ItemChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatRoomItem) {
            binding.otherUsernameTextView.text = item.otherUsername
            binding.lastMessageTextView.text = item.lastMessage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomListViewHolder {
        return ChatRoomListViewHolder(
            ItemChatRoomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatRoomListViewHolder, position: Int) {
        val chatRoom = currentList[position]
        holder.bind(chatRoom)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatRoomItem>() {
            override fun areItemsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem.chatRoomId == newItem.chatRoomId
            }

            override fun areContentsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}