package com.example.chat.userlist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.DBKey.Companion.DB_USERS
import com.example.chat.R
import com.example.chat.databinding.FragmentUserListBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserListFragment : Fragment(R.layout.fragment_user_list) {
    private lateinit var binding: FragmentUserListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserListBinding.bind(view)

        val userListAdapter = UserListAdapter()
        val currentUserId = Firebase.auth.currentUser?.uid ?: ""

        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }

        Firebase.database.reference.child(DB_USERS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userList = mutableListOf<UserItem>()
                    snapshot.children.forEach {
                        val user = it.getValue(UserItem::class.java) ?: return
                        if (user.userId != currentUserId){
                            userList.add(user)
                        }
                    }
                    userListAdapter.submitList(userList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "데이터를 읽어올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("USER", error.toString())
                }
            })
    }
}