package com.example.chat.chatroom

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.ItemChatBinding
import com.example.chat.userlist.UserItem

class ChatRoomAdapter : ListAdapter<ChatItem, ChatRoomAdapter.ChatRoomViewHolder>(diffUtil) {
    val otherUser: UserItem? = null

    inner class ChatRoomViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            if (item.userId == otherUser?.userId) {
                binding.usernameTextView.isVisible = true
                binding.usernameTextView.text = otherUser?.username

                binding.messageTextView.text = item.message
                binding.messageTextView.gravity = Gravity.START
            } else {
                binding.usernameTextView.isVisible = false

                binding.messageTextView.text = item.message
                binding.messageTextView.gravity = Gravity.END
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chat = currentList[position]
        holder.bind(chat)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}