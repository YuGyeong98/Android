package com.example.chat.chatroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.databinding.ActivityChatRoomBinding

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        const val CHAT_ROOM_ID = "CHAT_ROOM_ID"
        const val OTHER_USER_ID = "OTHER_USER_ID"
    }
}