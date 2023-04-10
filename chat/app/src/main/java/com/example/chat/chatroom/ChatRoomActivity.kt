package com.example.chat.chatroom

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.DBKey.Companion.DB_CHATS
import com.example.chat.DBKey.Companion.DB_CHAT_ROOMS
import com.example.chat.DBKey.Companion.DB_USERS
import com.example.chat.R
import com.example.chat.databinding.ActivityChatRoomBinding
import com.example.chat.userlist.UserItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private var otherUserId = ""
    private var otherFcmToken = ""
    private var chatRoomId = ""
    private val chatList = mutableListOf<ChatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otherUserId = intent.getStringExtra(OTHER_USER_ID) ?: return
        chatRoomId = intent.getStringExtra(CHAT_ROOM_ID) ?: return
        val currentUserId = Firebase.auth.currentUser?.uid ?: ""
        var currentUsername = ""

        Firebase.database.reference.child(DB_USERS).child(currentUserId).get()
            .addOnSuccessListener {
                val currentUser = it.getValue(UserItem::class.java)
                currentUsername = currentUser?.username ?: ""

                getOtherUserData()
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

            val client = OkHttpClient()
            val notification = JSONObject().apply {
                put("title", getString(R.string.app_name))
                put("body", message)
            }
            val root = JSONObject().apply {
                put("to", otherFcmToken)
                put("notification", notification)
            }
            val body = root.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .header("Authorization", "key=${getString(R.string.fcm_server_key)}")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.e("CHAT", response.toString())
                }
            })

            binding.messageEditText.text.clear()
        }

        chatRoomAdapter = ChatRoomAdapter()
        binding.chatRoomRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatRoomAdapter
        }
    }

    private fun getOtherUserData() {
        Firebase.database.reference.child(DB_USERS).child(otherUserId).get().addOnSuccessListener {
            val otherUser = it.getValue(UserItem::class.java)
            otherFcmToken = otherUser?.fcmToken.orEmpty()
            chatRoomAdapter.otherUser = otherUser

            getChatData()
        }
    }

    private fun getChatData() {
        Firebase.database.reference.child(DB_CHATS).child(chatRoomId)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val chat = snapshot.getValue(ChatItem::class.java) ?: return
                    chatList.add(chat)
                    chatRoomAdapter.submitList(chatList.toMutableList())
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatRoomActivity, "데이터를 읽어올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("CHAT_ROOM", error.toString())
                }
            })
    }

    companion object {
        const val CHAT_ROOM_ID = "CHAT_ROOM_ID"
        const val OTHER_USER_ID = "OTHER_USER_ID"
    }
}