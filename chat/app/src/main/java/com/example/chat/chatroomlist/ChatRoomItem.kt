package com.example.chat.chatroomlist

data class ChatRoomItem(
    val chatRoomId: String? = null,
    val otherUserId: String? = null,
    val otherUsername: String? = null,
    val lastMessage: String? = null,
)