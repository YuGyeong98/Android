package com.example.chat.user

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.DBKey.Companion.DB_USERS
import com.example.chat.R
import com.example.chat.databinding.FragmentUserBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment(R.layout.fragment_user) {
    private lateinit var binding: FragmentUserBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserBinding.bind(view)

        val userAdapter = UserAdapter()
        val currentUserId = Firebase.auth.currentUser?.uid ?: ""

        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
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
                    userAdapter.submitList(userList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "데이터를 읽어올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("USER", error.toString())
                }
            })
    }
}