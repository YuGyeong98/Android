package com.example.chat.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.chat.LoginActivity
import com.example.chat.R
import com.example.chat.databinding.FragmentMyPageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment(R.layout.fragment_my_page) {
    private lateinit var binding: FragmentMyPageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyPageBinding.bind(view)

        binding.signOutButton.setOnClickListener {
            Firebase.auth.signOut()

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}