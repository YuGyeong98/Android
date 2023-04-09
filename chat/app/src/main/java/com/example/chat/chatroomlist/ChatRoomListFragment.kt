package com.example.chat.chatroomlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.DBKey.Companion.DB_CHAT_ROOMS
import com.example.chat.R
import com.example.chat.chatroom.ChatRoomActivity
import com.example.chat.chatroom.ChatRoomActivity.Companion.CHAT_ROOM_ID
import com.example.chat.chatroom.ChatRoomActivity.Companion.OTHER_USER_ID
import com.example.chat.databinding.FragmentChatRoomListBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRoomListFragment : Fragment(R.layout.fragment_chat_room_list) {
    private lateinit var binding: FragmentChatRoomListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatRoomListBinding.bind(view)

        val chatRoomListAdapter = ChatRoomListAdapter { chatRoom ->
            Intent(context, ChatRoomActivity::class.java).apply {
                putExtra(OTHER_USER_ID, chatRoom.otherUserId)
                putExtra(CHAT_ROOM_ID, chatRoom.chatRoomId)
                startActivity(this)
            }
        }
        val currentUserId = Firebase.auth.currentUser?.uid ?: return

        binding.chatRoomListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatRoomListAdapter
        }

        Firebase.database.reference.child(DB_CHAT_ROOMS).child(currentUserId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatRoomList = snapshot.children.map {
                        it.getValue(ChatRoomItem::class.java)
                    }
                    chatRoomListAdapter.submitList(chatRoomList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "데이터를 읽어올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("CHAT_ROOM", error.toString())
                }
            })
    }
}