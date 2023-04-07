package com.example.chat.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chat.DBKey.Companion.DB_DESCRIPTION
import com.example.chat.DBKey.Companion.DB_USERS
import com.example.chat.DBKey.Companion.DB_USER_NAME
import com.example.chat.LoginActivity
import com.example.chat.R
import com.example.chat.databinding.FragmentMyPageBinding
import com.example.chat.userlist.UserItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment(R.layout.fragment_my_page) {
    private lateinit var binding: FragmentMyPageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyPageBinding.bind(view)

        val currentUserId = Firebase.auth.currentUser?.uid ?: ""
        val currentUserDB = Firebase.database.reference.child(DB_USERS).child(currentUserId)

        currentUserDB.get().addOnSuccessListener {
            val currentUser = it.getValue(UserItem::class.java) ?: return@addOnSuccessListener
            binding.usernameEditText.setText(currentUser.username)
            binding.descriptionEditText.setText(currentUser.description)
        }
        binding.applyButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userUpdates = hashMapOf<String, Any>(
                DB_USER_NAME to username,
                DB_DESCRIPTION to description,
            )
            currentUserDB.updateChildren(userUpdates)

            Toast.makeText(context, "변경 사항이 적용되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.signOutButton.setOnClickListener {
            Firebase.auth.signOut()

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}