package com.example.image_album

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.image_album.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val images = (intent.getStringArrayExtra("images") ?: emptyArray()).map {
            FrameItem(Uri.parse(it))
        }
        val frameAdapter = FrameAdapter(images)
        binding.viewPager.adapter = frameAdapter
    }
}