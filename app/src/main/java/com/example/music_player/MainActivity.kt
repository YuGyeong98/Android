package com.example.music_player

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.music_player.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pauseButton.setOnClickListener { pause() }
        binding.playButton.setOnClickListener { play() }
        binding.stopButton.setOnClickListener { stop() }
    }

    private fun pause() {
        Intent(this, MediaPlayerService::class.java).also { intent ->
            intent.action = PAUSE
            startService(intent)
        }
    }

    private fun play() {
        Intent(this, MediaPlayerService::class.java).also { intent ->
            intent.action = PLAY
            startService(intent)
        }
    }

    private fun stop() {
        Intent(this, MediaPlayerService::class.java).also { intent ->
            intent.action = STOP
            startService(intent)
        }
    }
}