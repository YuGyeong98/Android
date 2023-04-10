package com.example.chat.userlist

data class UserItem(
    val userId: String? = null,
    val username: String? = null,
    val description: String? = null,
    val fcmToken: String? = null,
)