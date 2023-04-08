package com.example.chat.chatroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.DBKey.Companion.DB_CHATS
import com.example.chat.DBKey.Companion.DB_CHAT_ROOMS
import com.example.chat.DBKey.Companion.DB_USERS
import com.example.chat.databinding.ActivityChatRoomBinding
import com.example.chat.userlist.UserItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatRoomId = intent.getStringExtra(CHAT_ROOM_ID) ?: return
        val otherUserId = intent.getStringExtra(OTHER_USER_ID) ?: return
        val currentUserId = Firebase.auth.currentUser?.uid ?: ""
        var currentUsername = ""

        Firebase.database.reference.child(DB_USERS).child(currentUserId).get()
            .addOnSuccessListener {
                val currentUser = it.getValue(UserItem::class.java)
                currentUsername = currentUser?.username ?: ""
            }

        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString()
            if (message.isEmpty()) {
                binding.sendButton.isEnabled = false
            }

            val newChatItem = ChatItem(
                userId = currentUserId,
                message = message,
            )
            Firebase.database.reference.child(DB_CHATS).child(chatRoomId).push().apply {
                newChatItem.chatId = key
                setValue(newChatItem)
            }

            val chatUpdates = hashMapOf<String, Any>(
                "${DB_CHAT_ROOMS}/$currentUserId/$otherUserId/lastMessage" to message,
                "${DB_CHAT_ROOMS}/$otherUserId/$currentUserId/lastMessage" to message,
                "${DB_CHAT_ROOMS}/$otherUserId/$currentUserId/chatRoomId" to chatRoomId,
                "${DB_CHAT_ROOMS}/$otherUserId/$currentUserId/otherUserId" to currentUserId,
                "${DB_CHAT_ROOMS}/$otherUserId/$currentUserId/otherUsername" to currentUsername,
            )
            Firebase.database.reference.updateChildren(chatUpdates)
        }
    }

    companion object {
        const val CHAT_ROOM_ID = "CHAT_ROOM_ID"
        const val OTHER_USER_ID = "OTHER_USER_ID"
    }
}