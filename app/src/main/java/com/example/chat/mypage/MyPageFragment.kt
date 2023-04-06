package com.example.chat.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.chat.R
import com.example.chat.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment(R.layout.fragment_my_page) {
    private lateinit var binding: FragmentMyPageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyPageBinding.bind(view)
    }
}